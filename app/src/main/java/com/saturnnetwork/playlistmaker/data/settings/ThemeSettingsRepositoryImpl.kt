package com.saturnnetwork.playlistmaker.data.settings

import android.content.SharedPreferences
import com.saturnnetwork.playlistmaker.domain.settings.ThemeSettingsRepository

class ThemeSettingsRepositoryImpl(
    private val sharedPrefs: SharedPreferences
) : ThemeSettingsRepository {

    companion object {
        private const val KEY_THEME = "themeSwitcherPosition"
    }

    override fun getThemeSetting(): Boolean {
        return sharedPrefs.getBoolean(KEY_THEME, false)
    }

    override fun saveThemeSetting(enabled: Boolean) {
        sharedPrefs.edit().putBoolean(KEY_THEME, enabled).apply()
    }
}