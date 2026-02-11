package com.saturnnetwork.playlistmaker.player.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saturnnetwork.playlistmaker.medialibraries.domain.db.PlaylistInteractor
import com.saturnnetwork.playlistmaker.medialibraries.domain.db.TrackDBInteractor
import com.saturnnetwork.playlistmaker.medialibraries.domain.model.Playlist
import com.saturnnetwork.playlistmaker.player.domain.PlayerInteractor
import com.saturnnetwork.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.flow.MutableSharedFlow
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asSharedFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.launch
import kotlinx.serialization.encodeToString
import kotlinx.serialization.json.Json
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerViewModel(
    private val playerInteractor: PlayerInteractor,
    private val trackDBInteractor: TrackDBInteractor,
    private val playlistInteractor: PlaylistInteractor
) : ViewModel() {

    companion object {
        private const val UPDATE_INTERVAL_MS = 300L
    }


    private var playerScreenStateLiveData = MutableLiveData<PlayerScreenState>()
    fun observeScreenStateLiveData(): LiveData<PlayerScreenState> = playerScreenStateLiveData

    val track = MutableLiveData<Track>()
    var isFavorite = false
    private var timerJob: Job? = null

    fun setTrack(_track: Track) {
        track.value = _track
        if (_track.isFavorite) {
            isFavorite = true
        }
        preparePlayer(_track.previewUrl)
    }

    fun preparePlayer(url: String) {
        playerInteractor.preparePlayer(
            url,
            onPrepared = {
                playerScreenStateLiveData.postValue(PlayerScreenState(PlayerState.PREPARED, track.value, "00:00", isFavorite))
            },
            onCompletion = {
                playerScreenStateLiveData.postValue(PlayerScreenState(PlayerState.PREPARED, track.value, "00:00", isFavorite))
            })
    }

     fun startPlayer() {
         playerInteractor.play()
         val currentTrack = track.value
         val currentPosition = playerScreenStateLiveData.value?.playbackPosition
         playerScreenStateLiveData.postValue(
             PlayerScreenState(PlayerState.PLAYING, currentTrack, currentPosition, isFavorite)
         )
         startTimer()

    }

    private fun pausePlayer() {
        playerInteractor.pause()
        val currentTrack = track.value
        val currentPosition = playerScreenStateLiveData.value?.playbackPosition
        playerScreenStateLiveData.postValue(PlayerScreenState(PlayerState.PAUSED, currentTrack, currentPosition, isFavorite))
        timerJob?.cancel()
    }

    fun pauseOnBackground() {
        if (playerScreenStateLiveData.value?.playerState == PlayerState.PLAYING) {
            pausePlayer()
        }
    }

    fun playbackControl() {
        playerScreenStateLiveData.value?.let {
            when (it.playerState) {
                PlayerState.PREPARED -> startPlayer()
                PlayerState.PLAYING -> pausePlayer()
                PlayerState.PAUSED -> startPlayer()
            }
        }
    }


    private fun startTimer() {
        timerJob?.cancel()
        timerJob = viewModelScope.launch {
            while (true) {
                delay(UPDATE_INTERVAL_MS)

                val currentState = playerScreenStateLiveData.value
                if (currentState?.playerState != PlayerState.PLAYING) {
                    break
                }

                val positionMs = playerInteractor.getCurrentPosition()
                playerScreenStateLiveData.postValue(
                    currentState.copy(
                        playbackPosition = SimpleDateFormat(
                            "mm:ss",
                            Locale.getDefault()
                        ).format(positionMs)
                    )
                )
            }
        }
    }

    fun addToFavorites(track: Track) {
        val currentState = playerScreenStateLiveData.value
        if (isFavorite) {
            isFavorite = false
            if (currentState != null) {
                playerScreenStateLiveData.postValue(
                    currentState.copy(isFavorite = false)
                )
            }
            viewModelScope.launch {
                trackDBInteractor.deleteFromFavorite(track.trackId)
            }
        } else {
            isFavorite = true
            if (currentState != null) {
                playerScreenStateLiveData.postValue(
                    currentState.copy(isFavorite = true)
                )
            }
            viewModelScope.launch {
                trackDBInteractor.addTrackToFavorite(track)
            }
        }

    }

    override fun onCleared() {
        super.onCleared()
        pausePlayer()
        playerInteractor.release()
    }

    private val _playlist = MutableStateFlow<List<Playlist>>(emptyList())

    val playlist: StateFlow<List<Playlist>> =
        _playlist.asStateFlow()

    private val _eventFlow = MutableSharedFlow<String>()
    val eventFlow = _eventFlow.asSharedFlow()

    fun getAllPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists()
                .collect { playlists ->
                    _playlist.emit(playlists)
                }
        }
    }

    fun onClickItem(trackId: String, playlistId: Long) {
        viewModelScope.launch {
            val playlist = playlistInteractor
                .getPlaylistById(playlistId)
                .first()
            if (trackId in playlist.trackIds) {
                _eventFlow.emit("Трек уже добавлен в плейлист ${playlist.name}")
            } else {
                val updatedList: List<String> = playlist.trackIds + trackId
                val jsonString = Json.encodeToString(updatedList)
                playlistInteractor.insertTracksId(jsonString, playlistId)
                _eventFlow.emit("Добавлено в плейлист ${playlist.name}")
            }
        }
    }


}
