package com.saturnnetwork.playlistmaker.di

import com.saturnnetwork.playlistmaker.player.data.PlayerRepositoryImpl
import com.saturnnetwork.playlistmaker.medialibraries.data.db.converters.TrackDbConvertor
import com.saturnnetwork.playlistmaker.medialibraries.data.db.TrackDBRepositoryImpl
import com.saturnnetwork.playlistmaker.player.domain.PlayerRepository
import com.saturnnetwork.playlistmaker.medialibraries.domain.db.TrackDBRepository
import com.saturnnetwork.playlistmaker.search.data.TracksRepositoryImpl
import com.saturnnetwork.playlistmaker.search.domain.TracksRepository
import com.saturnnetwork.playlistmaker.settings.data.SettingsRepositoryImpl
import com.saturnnetwork.playlistmaker.settings.domain.SettingsRepository
import org.koin.dsl.module

val repositoryModule = module {

    factory<TracksRepository> {
        TracksRepositoryImpl(get(), get(), get(), get())
    }

    factory<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    factory<PlayerRepository> {
        PlayerRepositoryImpl(get())
    }

    factory { TrackDbConvertor() }

    single<TrackDBRepository> {
        TrackDBRepositoryImpl(get(), get())
    }
}