package com.saturnnetwork.playlistmaker.search.domain

// 1.TracksInteractor -> TracksInteractorImpl -> TracksRepository ->
// Это интерфейс бизнес-логики для поиска треков
// impl in class TracksInteractorImpl

import com.saturnnetwork.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import java.util.concurrent.Executors
import com.saturnnetwork.playlistmaker.search.domain.models.TracksResponse

class TracksInteractorImpl (private val repository: TracksRepository): TracksInteractor {


    override fun searchTracks(expression: String): Flow<TracksResponse> {
        return repository.searchTracks(expression)
    }


    // отправляем трек в repository (метод saveToHistory)
    override fun saveToHistory(track: Track) {
        repository.saveToHistory(track)
    }

    override fun loadFromHistory(): ArrayList<Track> {
        return repository.loadFromHistory()
    }

    override fun clearHistory() {
        repository.clearHistory()
    }


}