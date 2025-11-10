package com.saturnnetwork.playlistmaker.di

import android.content.SharedPreferences
import com.saturnnetwork.playlistmaker.data.TracksRepositoryImpl
import com.saturnnetwork.playlistmaker.data.network.RetrofitNetworkClient
import com.saturnnetwork.playlistmaker.domain.TracksInteractor
import com.saturnnetwork.playlistmaker.domain.TracksInteractorImpl

// Создатель интерактора
object TracksInteractorCreator {
    fun create(sharedPrefs: SharedPreferences): TracksInteractor {
        val networkClient = RetrofitNetworkClient()
        val repository = TracksRepositoryImpl(networkClient, sharedPrefs)
        return TracksInteractorImpl(repository)
    }
}
