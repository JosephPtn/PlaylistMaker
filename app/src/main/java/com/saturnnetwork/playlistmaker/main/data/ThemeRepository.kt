package com.saturnnetwork.playlistmaker.main.data

import android.content.Context
import androidx.core.content.edit

class ThemeRepository(context: Context) {
    private val sharedPrefs = context.getSharedPreferences("settings_activity_preferences", Context.MODE_PRIVATE)

    fun isDarkThemeEnabled(): Boolean {
        return sharedPrefs.getBoolean("themeSwitcherPosition", false)
    }
}
