package com.saturnnetwork.playlistmaker.domain

// 1.TracksInteractor -> TracksInteractorImpl -> TracksRepository ->
// Это интерфейс бизнес-логики для поиска треков
// impl in class TracksInteractorImpl
import com.saturnnetwork.playlistmaker.domain.models.Track

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