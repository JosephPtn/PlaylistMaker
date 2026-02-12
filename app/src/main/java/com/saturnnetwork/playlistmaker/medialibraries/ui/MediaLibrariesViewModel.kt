package com.saturnnetwork.playlistmaker.medialibraries.ui

import android.net.Uri
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saturnnetwork.playlistmaker.medialibraries.domain.db.PlaylistInteractor
import com.saturnnetwork.playlistmaker.medialibraries.domain.db.TrackDBInteractor
import com.saturnnetwork.playlistmaker.medialibraries.domain.model.Playlist
import com.saturnnetwork.playlistmaker.medialibraries.ui.CreatePlaylist.CreatePlaylistScreenState
import com.saturnnetwork.playlistmaker.medialibraries.ui.Playlist.FragmentPlaylistScreenState
import com.saturnnetwork.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MediaLibrariesViewModel(private val interactor: TrackDBInteractor,
    private val playlistInteractor: PlaylistInteractor): ViewModel() {

    private val _tracksLiveData = MutableLiveData<List<Track>>()
    val tracksLiveData: LiveData<List<Track>> = _tracksLiveData

    fun init() {
        getAllTrackFromFavorite()
    }

    fun getAllTrackFromFavorite() {
        viewModelScope.launch {
            interactor.getAllTrackFromFavorite().collect { tracks ->
                _tracksLiveData.value = tracks
            }
        }
    }

    private val _cPViewState = MutableStateFlow(
        CreatePlaylistScreenState(
            playListCover = Uri.EMPTY,
            playListCoverTag = 0,
            createBtnActive = false,
            createPlaylist = false
        )
    )
    val cPViewState: StateFlow<CreatePlaylistScreenState> = _cPViewState.asStateFlow()

    fun setDefaultCoverIfEmpty(uri: Uri) {
        if (_cPViewState.value.playListCover == Uri.EMPTY) {
            _cPViewState.update { currentState ->
                currentState.copy(playListCover = uri, playListCoverTag = 0)
            }
        }
    }

    fun setCoverUri(uri: Uri) {
        _cPViewState.update { currentState ->
            currentState.copy(playListCover = uri, playListCoverTag = -1)
        }
    }

    fun setPlaylistName(name: String) {
        /*if (name.isNotEmpty() && name.isNotBlank()) {
            _cPViewState.update { currentState ->
                currentState.copy(createBtnActive = true)
            }
        } else {
            _cPViewState.update { currentState ->
                currentState.copy(createBtnActive = false)
            }
        }*/
        _cPViewState.update { currentState ->
            currentState.copy(createBtnActive = name.isNotEmpty() && name.isNotBlank())
        }
    }

     fun writePlaylistToDatabase(name: String, description: String) {
         viewModelScope.launch {
             var coverStr = ""
             if (_cPViewState.value.playListCoverTag != 0) {
                 coverStr = _cPViewState.value.playListCover.toString()
             }
             playlistInteractor.addPlaylist(Playlist(
                 id = 0,
                 name = name,
                 description = description,
                 coverImagePath = coverStr,
                 trackIds = arrayListOf(),
                 trackCount = 0
             ))
             _cPViewState.update { currentState ->
                 currentState.copy(createPlaylist = true)
             }
         }
    }


    private var _playlist = MutableLiveData<List<Playlist>>(emptyList())
    fun observePlaylist(): LiveData<List<Playlist>> = _playlist

    fun getAllPlaylists() {
        viewModelScope.launch {
            playlistInteractor.getAllPlaylists()
                .collect { playlists ->
                    _playlist.postValue(playlists)
                }
        }
    }

}