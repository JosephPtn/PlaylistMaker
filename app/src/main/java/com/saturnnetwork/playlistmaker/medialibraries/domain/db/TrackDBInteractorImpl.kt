package com.saturnnetwork.playlistmaker.medialibraries.domain.db

import com.saturnnetwork.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

class TrackDBInteractorImpl(private val repository: TrackDBRepository): TrackDBInteractor {
    override suspend fun addTrackToFavorite(track: Track) {
        repository.addTrackToFavorite(track)
    }

    override suspend fun deleteFromFavorite(id: String) {
        repository.deleteFromFavorite(id)
    }

    override fun getAllTrackFromFavorite(): Flow<List<Track>> {
        return repository.getAllTrackFromFavorite()
    }
}