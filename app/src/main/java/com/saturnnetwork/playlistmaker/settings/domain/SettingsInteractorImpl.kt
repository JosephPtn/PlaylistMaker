package com.saturnnetwork.playlistmaker.settings.domain


class SettingsInteractorImpl(
    private val repository: SettingsRepository
) : SettingsInteractor {

    override fun isDarkModeEnabled(): Boolean {
        return repository.getThemeSetting()
    }

    override fun setDarkMode(enabled: Boolean) {
        repository.saveThemeSetting(enabled)
    }

    override fun buttonToShareApp(): Int {
        return repository.buttonToShareApp()
    }


    override fun buttonToContactSupport(): List<Int>  {
        return repository.buttonToContactSupport()
    }

    override fun buttonToUserAgreement(): Int {
        return repository.buttonToUserAgreement()
    }


}