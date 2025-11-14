package com.saturnnetwork.playlistmaker.creator

import android.content.Context
import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.saturnnetwork.playlistmaker.di.SettingsInteractorCreator
import com.saturnnetwork.playlistmaker.settings.ui.ResourceProviderImpl
import com.saturnnetwork.playlistmaker.settings.ui.SettingViewModel


class SettingViewModelFactory(
    private val sharedPreferences: SharedPreferences,
    private val context: Context
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SettingViewModel::class.java)) {
            val settingInteractor = SettingsInteractorCreator.create(sharedPreferences)
            val resourceProvider = ResourceProviderImpl(context)
            @Suppress("UNCHECKED_CAST")
            return SettingViewModel(settingInteractor, resourceProvider) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
