package com.saturnnetwork.playlistmaker.domain.settings

import com.saturnnetwork.playlistmaker.domain.settings.ThemeSettingsRepository

class ThemeSettingsInteractorImpl(
    private val repository: ThemeSettingsRepository
) : ThemeSettingsInteractor {

    override fun isDarkModeEnabled(): Boolean {
        return repository.getThemeSetting()
    }

    override fun setDarkMode(enabled: Boolean) {
        repository.saveThemeSetting(enabled)
    }
}