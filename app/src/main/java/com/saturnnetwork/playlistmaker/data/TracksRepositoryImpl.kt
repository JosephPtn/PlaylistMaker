package com.saturnnetwork.playlistmaker.data

// Делает сетевой запрос, получает данные от API

/*
Это репозиторий — он знает, как получить данные
Он:
Делает запрос через networkClient
Проверяет resultCode
Преобразует TrackDTO → Track
Возвращает список ArrayList<Track>

Зачем нужен?
Чтобы UI не зависал (все запросы идут в фоне)
Чтобы отделить бизнес-логику от запроса
 */

import android.content.SharedPreferences
import com.saturnnetwork.playlistmaker.data.dto.SearchRequest
import com.saturnnetwork.playlistmaker.data.dto.SearchResponse
import com.saturnnetwork.playlistmaker.data.dto.TrackDTO
import com.saturnnetwork.playlistmaker.domain.TracksRepository
import com.saturnnetwork.playlistmaker.domain.models.Track
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class TracksRepositoryImpl (private val networkClient: NetworkClient,
                            private val sharedPrefs: SharedPreferences
): TracksRepository {

    private val gson = Gson()
    private val key = "prefsHistory"


    fun TrackDTO.toTrack(): Track = Track(
        trackId = this.trackId,
        trackName = this.trackName,
        artistName = this.artistName,
        trackTime = this.trackTimeMillis,
        artworkUrl100 = this.artworkUrl100,
        collectionName = this.collectionName,
        releaseDate = this.releaseDate,
        primaryGenreName = this.primaryGenreName,
        country = this.country,
        previewUrl = this.previewUrl
    )

    override fun searchTracks(expression: String): ArrayList<Track> {
        val response = networkClient.doRequest(SearchRequest(expression))
        if (response.resultCode == 200) {
            val tracks =(response as SearchResponse).results.map { it.toTrack() } as ArrayList<Track>
            return tracks
        } else {
            return ArrayList()
        }
    }

    override fun saveToHistory(track: Track) {
        val currentHistory = loadFromHistory()
        currentHistory.removeAll { it.trackId == track.trackId }
        currentHistory.add(0, track)
        if (currentHistory.size > 10) currentHistory.removeLast()

        val json = gson.toJson(currentHistory)
        sharedPrefs.edit().putString(key, json).apply()
    }

    override fun loadFromHistory(): ArrayList<Track> {
        val json = sharedPrefs.getString(key, null) ?: return ArrayList()
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return gson.fromJson(json, type)
    }

    override fun clearHistory() {
        sharedPrefs.edit().remove(key).apply()
    }

}