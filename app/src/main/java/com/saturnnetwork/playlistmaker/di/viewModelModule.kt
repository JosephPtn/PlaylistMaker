package com.saturnnetwork.playlistmaker.di

import com.saturnnetwork.playlistmaker.medialibraries.ui.MediaLibrariesViewModel
import com.saturnnetwork.playlistmaker.player.ui.PlayerViewModel
import com.saturnnetwork.playlistmaker.search.ui.SearchViewModel
import com.saturnnetwork.playlistmaker.settings.ui.SettingViewModel
import org.koin.core.module.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        SettingViewModel(get(), get())
    }

    viewModel {
        PlayerViewModel(get(), get(), get())
    }

    viewModel {
        MediaLibrariesViewModel(get(), get())
    }


}