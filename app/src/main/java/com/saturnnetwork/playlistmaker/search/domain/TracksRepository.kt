package com.saturnnetwork.playlistmaker.search.domain

import com.saturnnetwork.playlistmaker.search.domain.models.Track
import com.saturnnetwork.playlistmaker.search.domain.models.TracksResponse
import kotlinx.coroutines.flow.Flow

interface TracksRepository {

    fun searchTracks(expression: String): Flow<TracksResponse>

    // inmpl in TracksRepositoryImpl
    fun saveToHistory(track: Track)

    // inmpl in TracksRepositoryImpl
    fun loadFromHistory(): ArrayList<Track>

    fun clearHistory()

}