package com.saturnnetwork.playlistmaker.search.domain

import com.saturnnetwork.playlistmaker.search.domain.models.Track

interface TracksInteractor {
    fun searchTracks(expression: String, consumer: TracksConsumer)

    fun saveToHistory(track: Track)

    fun loadFromHistory(): ArrayList<Track>

    fun clearHistory()


    // Это интерфейс-обратный вызов (callback), который получает список треков:
    interface TracksConsumer {
        fun consume(foundTracks: ArrayList<Track>)
        fun onError()
    }

}