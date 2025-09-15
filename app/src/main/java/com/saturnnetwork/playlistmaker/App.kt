package com.saturnnetwork.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val sharedPrefs = getSharedPreferences("settings_activity_preferences", MODE_PRIVATE)
        val themeSwitcherPosition: Boolean = sharedPrefs.getBoolean("themeSwitcherPosition", false)
        switchTheme(themeSwitcherPosition)

    }

    fun switchTheme(darkThemeEnabled: Boolean) {

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}