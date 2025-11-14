package com.saturnnetwork.playlistmaker.settings.domain

import android.content.Intent

interface SettingsInteractor {
    fun isDarkModeEnabled(): Boolean
    fun setDarkMode(enabled: Boolean)
    fun buttonToShareApp(): Int
    fun buttonToContactSupport(): List<Int>
    fun buttonToUserAgreement(): Int
}