package com.saturnnetwork.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import com.saturnnetwork.playlistmaker.main.data.ThemeRepository

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        val themeRepository = ThemeRepository(this)
        val darkTheme = themeRepository.isDarkThemeEnabled()
        switchTheme(darkTheme)

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