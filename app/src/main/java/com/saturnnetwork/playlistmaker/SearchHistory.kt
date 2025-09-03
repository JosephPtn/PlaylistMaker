package com.saturnnetwork.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import androidx.core.content.edit
import com.google.gson.reflect.TypeToken

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