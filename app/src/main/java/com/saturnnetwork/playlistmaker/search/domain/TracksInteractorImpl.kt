package com.saturnnetwork.playlistmaker.search.domain

// 1.TracksInteractor -> TracksInteractorImpl -> TracksRepository ->
// Это интерфейс бизнес-логики для поиска треков
// impl in class TracksInteractorImpl

import com.saturnnetwork.playlistmaker.search.domain.models.Track
import java.util.concurrent.Executors

class TracksInteractorImpl (private val repository: TracksRepository): TracksInteractor {
    private val executor = Executors.newCachedThreadPool()

    override fun searchTracks(expression: String, consumer: TracksInteractor.TracksConsumer) {
        executor.execute {
            try {
                val result = repository.searchTracks(expression)
                consumer.consume(result) // даже если пустой список — это успешный поиск
            } catch (e: Exception) {
                consumer.onError() // только настоящая ошибка сети
            }
        }
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