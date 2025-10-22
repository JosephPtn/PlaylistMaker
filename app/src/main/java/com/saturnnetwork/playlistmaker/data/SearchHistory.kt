package com.saturnnetwork.playlistmaker.data

import android.content.SharedPreferences
import androidx.core.content.edit
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.saturnnetwork.playlistmaker.domain.models.Track

class SearchHistory(private val prefsHistory: SharedPreferences) {

    fun clear() {
        prefsHistory.edit { clear() }
    }

    fun read(): ArrayList<Track> {
        val json = prefsHistory.getString("prefsHistory", null) ?: return ArrayList<Track>()
        val type = object : TypeToken<ArrayList<Track>>() {}.type
        return Gson().fromJson(json, type)
    }

    fun write(track: Track) {
        val trackList: ArrayList<Track> = read()
        trackList.removeIf { it.trackId == track.trackId }
        if (trackList.size < 10) {
            trackList.add(0, track)
        } else {
            trackList.removeAt(9)
            trackList.add(0, track)
        }
        val json = Gson().toJson(trackList)
        prefsHistory.edit {
            putString("prefsHistory", json)
        }
    }

}