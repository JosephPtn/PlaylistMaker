package com.saturnnetwork.playlistmaker.di

import com.saturnnetwork.playlistmaker.player.data.PlayerRepositoryImpl
import com.saturnnetwork.playlistmaker.player.domain.PlayerRepository
import com.saturnnetwork.playlistmaker.search.data.TracksRepositoryImpl
import com.saturnnetwork.playlistmaker.search.domain.TracksRepository
import com.saturnnetwork.playlistmaker.settings.data.SettingsRepositoryImpl
import com.saturnnetwork.playlistmaker.settings.domain.SettingsRepository
import org.koin.core.qualifier.named
import org.koin.dsl.module

val repositoryModule = module {

    single<TracksRepository> {
        TracksRepositoryImpl(get(), get(), get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    single<PlayerRepository> {
        PlayerRepositoryImpl(get())
    }
}