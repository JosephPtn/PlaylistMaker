package com.saturnnetwork.playlistmaker.domain.settings

import android.content.SharedPreferences

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