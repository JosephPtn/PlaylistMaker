package com.saturnnetwork.playlistmaker.search.domain

import com.saturnnetwork.playlistmaker.search.domain.models.Track

interface TracksRepository {

    fun searchTracks(expression: String): ArrayList<Track>

    // inmpl in TracksRepositoryImpl
    fun saveToHistory(track: Track)

    // inmpl in TracksRepositoryImpl
    fun loadFromHistory(): ArrayList<Track>

    fun clearHistory()

}