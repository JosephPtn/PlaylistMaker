package com.saturnnetwork.playlistmaker.search.data

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
import android.os.Build
import androidx.annotation.RequiresApi
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.saturnnetwork.playlistmaker.medialibraries.data.db.AppDatabase
import com.saturnnetwork.playlistmaker.search.domain.TracksRepository
import com.saturnnetwork.playlistmaker.search.domain.models.Track
import com.saturnnetwork.playlistmaker.search.data.dto.SearchRequest
import com.saturnnetwork.playlistmaker.search.data.dto.SearchResponse
import com.saturnnetwork.playlistmaker.search.domain.models.TracksResponse
import com.saturnnetwork.playlistmaker.search.data.dto.TrackDTO
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.first
import kotlinx.coroutines.flow.flow
import kotlin.collections.removeAll

class TracksRepositoryImpl (private val networkClient: NetworkClient,
                            private val sharedPrefs: SharedPreferences,
                            private val gson: Gson,
                            private val appDatabase: AppDatabase
): TracksRepository {

    // тут указываем ключ из файла (/data/data/<your.package.name>/shared_prefs/SharedPrefs.xml)
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


    override fun searchTracks(expression: String): Flow<TracksResponse> = flow {
        val response = networkClient.doRequest(SearchRequest(expression))
        val allTrackFromFavorite =
            appDatabase.trackDao().getTracks().first()

        if (response.resultCode == 200) {
            val searchResponse = (response as SearchResponse).results.map { it.toTrack() } as ArrayList<Track>
            searchResponse.forEach { track ->
                for (fav in allTrackFromFavorite) {
                    if (fav.trackId == track.trackId) {
                        track.isFavorite = true
                        break
                    }
                }
            }
            emit(TracksResponse(searchResponse, 200))
        } else {
            emit(TracksResponse(ArrayList(), response.resultCode))
        }

    }


    @RequiresApi(Build.VERSION_CODES.VANILLA_ICE_CREAM)
    override suspend fun saveToHistory(track: Track) {
        val currentHistory = loadFromHistory()
        currentHistory.removeAll { it.trackId == track.trackId }
        currentHistory.add(0, track)
        if (currentHistory.size > 10) currentHistory.removeLast()

        val json = gson.toJson(currentHistory)
        sharedPrefs.edit().putString(key, json).apply()
    }

    override suspend fun loadFromHistory(): ArrayList<Track> {
        val allTrackFromFavorite =
            appDatabase.trackDao().getTracks().first()
        val json = sharedPrefs.getString(key, null) ?: return ArrayList()
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        val history: ArrayList<Track> =  gson.fromJson(json, type)
        history.forEach { track ->
            for (fav in allTrackFromFavorite) {
                if (fav.trackId == track.trackId) {
                    track.isFavorite = true
                    break
                }
            }
        }
        return history

    }

    override fun clearHistory() {
        sharedPrefs.edit().remove(key).apply()
    }

    override suspend fun isFavoriteTrack(track: Track): Boolean {
        val allTrackFromFavorite =
            appDatabase.trackDao().getTracks().first()
        for (fav in allTrackFromFavorite) {
            if (fav.trackId == track.trackId) {
                track.isFavorite = true
                return true
            }
        }
        return false
    }

}