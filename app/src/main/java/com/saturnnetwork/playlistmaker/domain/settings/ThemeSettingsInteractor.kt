package com.saturnnetwork.playlistmaker.domain.settings

interface ThemeSettingsInteractor {
    fun isDarkModeEnabled(): Boolean
    fun setDarkMode(enabled: Boolean)
}