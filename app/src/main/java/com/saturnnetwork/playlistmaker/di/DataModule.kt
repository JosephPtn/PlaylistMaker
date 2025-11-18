package com.saturnnetwork.playlistmaker.di

import android.content.Context
import android.media.MediaPlayer
import com.google.gson.Gson
import com.saturnnetwork.playlistmaker.main.data.ThemeRepository
import com.saturnnetwork.playlistmaker.search.data.NetworkClient
import com.saturnnetwork.playlistmaker.search.data.network.ItunesApiService
import com.saturnnetwork.playlistmaker.search.data.network.RetrofitNetworkClient
import com.saturnnetwork.playlistmaker.settings.domain.SettingsInteractor
import com.saturnnetwork.playlistmaker.settings.domain.SettingsInteractorImpl
import com.saturnnetwork.playlistmaker.settings.ui.ResourceProvider
import com.saturnnetwork.playlistmaker.settings.ui.ResourceProviderImpl
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

val dataModule = module {

    single<ItunesApiService> {
        Retrofit.Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesApiService::class.java)
    }

    // используется: SearchViewModel, SettingViewModel
    single {
        androidContext()
            // тут мы указываем файл (/data/data/<your.package.name>/shared_prefs/SharedPrefs.xml)
            .getSharedPreferences("SharedPrefs", Context.MODE_PRIVATE)
    }

    factory { Gson() }

    single<NetworkClient> {
        RetrofitNetworkClient(get())
    }

    single<ResourceProvider> {
        ResourceProviderImpl(androidContext())
    }

    factory { ThemeRepository(androidContext()) }

    factory { MediaPlayer() }


}