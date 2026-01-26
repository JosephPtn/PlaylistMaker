package com.saturnnetwork.playlistmaker.search.domain

import com.saturnnetwork.playlistmaker.search.domain.models.Track
import com.saturnnetwork.playlistmaker.search.domain.models.TracksResponse
import kotlinx.coroutines.flow.Flow

interface TracksInteractor {

    fun searchTracks(expression: String): Flow<TracksResponse>

    fun saveToHistory(track: Track)

    fun loadFromHistory(): ArrayList<Track>

    fun clearHistory()


}