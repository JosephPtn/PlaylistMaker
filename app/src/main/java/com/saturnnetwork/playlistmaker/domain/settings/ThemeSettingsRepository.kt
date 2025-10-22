package com.saturnnetwork.playlistmaker.domain.settings

interface ThemeSettingsRepository {
    fun getThemeSetting(): Boolean
    fun saveThemeSetting(enabled: Boolean)
}