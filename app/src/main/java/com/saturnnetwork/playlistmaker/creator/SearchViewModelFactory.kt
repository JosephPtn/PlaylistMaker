package com.saturnnetwork.playlistmaker.creator

import android.content.SharedPreferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.saturnnetwork.playlistmaker.di.SettingsInteractorCreator
import com.saturnnetwork.playlistmaker.di.TracksInteractorCreator
import com.saturnnetwork.playlistmaker.search.ui.SearchViewModel
import com.saturnnetwork.playlistmaker.settings.ui.SettingViewModel

class SearchViewModelFactory(
    private val sharedPreferences: SharedPreferences
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(SearchViewModel::class.java)) {
            val searchInteractor = TracksInteractorCreator.create(sharedPreferences)
            @Suppress("UNCHECKED_CAST")
            return SearchViewModel(searchInteractor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}