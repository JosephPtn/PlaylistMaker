package com.saturnnetwork.playlistmaker.domain

// Передаёт запрос в репозиторий, работает с фоном
import com.saturnnetwork.playlistmaker.domain.models.Track
import java.util.concurrent.Executors

class TracksInteractorImpl (private val repository: TracksRepository): TracksInteractor {
    private val executor = Executors.newCachedThreadPool()
    override fun searchTracks(
        expression: String,
        consumer: TracksInteractor.TracksConsumer
    ) {
        executor.execute {
            val result = repository.searchTracks(expression)
            if (result.isNotEmpty()) {
                consumer.consume(result)
            } else {
                consumer.onError()
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
