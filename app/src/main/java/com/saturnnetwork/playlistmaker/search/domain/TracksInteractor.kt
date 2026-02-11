package com.saturnnetwork.playlistmaker.search.domain

import com.saturnnetwork.playlistmaker.search.domain.models.Track
import com.saturnnetwork.playlistmaker.search.domain.models.TracksResponse
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {

    fun searchTracks(expression: String): Flow<TracksResponse>

    suspend fun saveToHistory(track: Track)

    suspend fun loadFromHistory(): ArrayList<Track>

    fun clearHistory()

    suspend fun isFavoriteTrack(track: Track): Boolean


}