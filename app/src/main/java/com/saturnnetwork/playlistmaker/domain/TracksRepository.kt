package com.saturnnetwork.playlistmaker.domain

import com.saturnnetwork.playlistmaker.domain.models.Track


interface TracksRepository {

    fun searchTracks(expression: String): ArrayList<Track>

    // inmpl in TracksRepositoryImpl
    fun saveToHistory(track: Track)

    // inmpl in TracksRepositoryImpl
    fun loadFromHistory(): ArrayList<Track>

    fun clearHistory()

}