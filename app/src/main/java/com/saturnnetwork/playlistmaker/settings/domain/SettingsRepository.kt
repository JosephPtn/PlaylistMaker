package com.saturnnetwork.playlistmaker.settings.domain

import android.content.Intent

interface SettingsRepository {
    fun getThemeSetting(): Boolean
    fun saveThemeSetting(enabled: Boolean)
    fun buttonToShareApp(value: String): Intent
    fun buttonToContactSupport(email: String, subject: String, text: String): Intent
    fun buttonToUserAgreement(url: String): Intent
}