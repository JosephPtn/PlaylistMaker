package com.saturnnetwork.playlistmaker.medialibraries.domain.db

import com.saturnnetwork.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface TrackDBRepository {
    suspend fun addTrackToFavorite(track: Track)
    suspend fun deleteFromFavorite(id: String)
    fun getAllTrackFromFavorite(): Flow<List<Track>>
}