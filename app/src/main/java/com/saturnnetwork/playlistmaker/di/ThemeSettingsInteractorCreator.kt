package com.saturnnetwork.playlistmaker.di

import android.content.SharedPreferences
import com.saturnnetwork.playlistmaker.domain.settings.ThemeSettingsInteractor
import com.saturnnetwork.playlistmaker.domain.settings.ThemeSettingsInteractorImpl
import com.saturnnetwork.playlistmaker.domain.settings.ThemeSettingsRepositoryImpl

// Создатель интерактора
object ThemeSettingsInteractorCreator {
    fun create(sharedPreferences: SharedPreferences): ThemeSettingsInteractor {
        val repository = ThemeSettingsRepositoryImpl(sharedPreferences)
        return ThemeSettingsInteractorImpl(repository)
    }
}
