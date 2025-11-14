package com.saturnnetwork.playlistmaker.settings.domain

import android.content.Intent

interface SettingsRepository {
    fun getThemeSetting(): Boolean
    fun saveThemeSetting(enabled: Boolean)
    fun buttonToShareApp(): Int
    fun buttonToContactSupport(): List<Int>
    fun buttonToUserAgreement(): Int
}