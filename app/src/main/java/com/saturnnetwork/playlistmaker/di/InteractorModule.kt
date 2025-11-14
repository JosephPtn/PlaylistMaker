package com.saturnnetwork.playlistmaker.di

import com.saturnnetwork.playlistmaker.player.domain.PlayerInteractor
import com.saturnnetwork.playlistmaker.player.domain.PlayerInteractorImpl
import com.saturnnetwork.playlistmaker.search.domain.TracksInteractor
import com.saturnnetwork.playlistmaker.search.domain.TracksInteractorImpl
import com.saturnnetwork.playlistmaker.settings.domain.SettingsInteractor
import com.saturnnetwork.playlistmaker.settings.domain.SettingsInteractorImpl
import org.koin.dsl.module

val interactorModule = module {

    factory<TracksInteractor> {
        TracksInteractorImpl(get())
    }

    // используется: SettingViewModel
    single<SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }

}