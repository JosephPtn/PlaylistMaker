package com.saturnnetwork.playlistmaker.di

import android.content.SharedPreferences
import com.saturnnetwork.playlistmaker.settings.domain.SettingsInteractor
import com.saturnnetwork.playlistmaker.settings.domain.SettingsInteractorImpl
import com.saturnnetwork.playlistmaker.settings.data.SettingsRepositoryImpl

// Создатель интерактора
object SettingsInteractorCreator {
    fun create(sharedPreferences: SharedPreferences): SettingsInteractor {
        val repository = SettingsRepositoryImpl(sharedPreferences)
        return SettingsInteractorImpl(repository)
    }
}
