package com.saturnnetwork.playlistmaker.settings.domain

import android.content.Intent

class SettingsInteractorImpl(
    private val repository: SettingsRepository
) : SettingsInteractor {

    override fun isDarkModeEnabled(): Boolean {
        return repository.getThemeSetting()
    }

    override fun setDarkMode(enabled: Boolean) {
        repository.saveThemeSetting(enabled)
    }

    override fun buttonToShareApp(value: String): Intent {
        return repository.buttonToShareApp(value)
    }

    override fun buttonToContactSupport(
        email: String,
        subject: String,
        text: String
    ): Intent {
        return repository.buttonToContactSupport(email,subject,text)
    }

    override fun buttonToUserAgreement(url: String): Intent {
        return repository.buttonToUserAgreement(url)
    }


}