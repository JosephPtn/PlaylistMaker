package com.saturnnetwork.playlistmaker.settings.domain

import android.content.Intent

interface SettingsInteractor {
    fun isDarkModeEnabled(): Boolean
    fun setDarkMode(enabled: Boolean)
    fun buttonToShareApp(value: String): Intent
    fun buttonToContactSupport(email: String, subject: String, text: String): Intent
    fun buttonToUserAgreement(url: String): Intent
}