package com.saturnnetwork.playlistmaker.medialibraries.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saturnnetwork.playlistmaker.medialibraries.domain.db.TrackDBInteractor
import com.saturnnetwork.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.launch

class MediaLibrariesViewModel(private val interactor: TrackDBInteractor): ViewModel() {

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


}