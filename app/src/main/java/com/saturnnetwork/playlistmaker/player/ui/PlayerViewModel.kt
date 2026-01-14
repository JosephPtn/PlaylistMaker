package com.saturnnetwork.playlistmaker.player.ui

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saturnnetwork.playlistmaker.player.domain.PlayerInteractor
import com.saturnnetwork.playlistmaker.player.domain.PlayerState
import com.saturnnetwork.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch
import java.text.SimpleDateFormat
import java.util.Locale


class PlayerViewModel(
    private val playerInteractor: PlayerInteractor
) : ViewModel() {

    companion object {
        private const val UPDATE_INTERVAL_MS = 300L
    }


    private var playerScreenStateLiveData = MutableLiveData<PlayerScreenState>()
    fun observeScreenStateLiveData(): LiveData<PlayerScreenState> = playerScreenStateLiveData

    val track = MutableLiveData<Track>()
    private var timerJob: Job? = null

    fun setTrack(_track: Track) {
        track.value = _track
        preparePlayer(_track.previewUrl)
    }

    fun preparePlayer(url: String) {
        playerInteractor.preparePlayer(
            url,
            onPrepared = {
                playerScreenStateLiveData.postValue(PlayerScreenState(PlayerState.PREPARED, track.value, "00:00"))
            },
            onCompletion = {
                playerScreenStateLiveData.postValue(PlayerScreenState(PlayerState.PREPARED, track.value, "00:00"))
            })
    }

     fun startPlayer() {
         playerInteractor.play()
         val currentTrack = track.value
         val currentPosition = playerScreenStateLiveData.value?.playbackPosition
         playerScreenStateLiveData.postValue(
             PlayerScreenState(PlayerState.PLAYING, currentTrack, currentPosition)
         )
         startTimer()

    }

    private fun pausePlayer() {
        playerInteractor.pause()
        val currentTrack = track.value
        val currentPosition = playerScreenStateLiveData.value?.playbackPosition
        playerScreenStateLiveData.postValue(PlayerScreenState(PlayerState.PAUSED, currentTrack, currentPosition))
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


    override fun onCleared() {
        super.onCleared()
        pausePlayer()
        playerInteractor.release()
    }

}
