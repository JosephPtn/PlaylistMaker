package com.saturnnetwork.playlistmaker

import android.app.Application
import androidx.appcompat.app.AppCompatDelegate
import org.koin.android.ext.koin.androidContext
import org.koin.android.ext.android.inject
import org.koin.core.context.startKoin
import com.saturnnetwork.playlistmaker.di.dataModule
import com.saturnnetwork.playlistmaker.di.interactorModule
import com.saturnnetwork.playlistmaker.di.repositoryModule
import com.saturnnetwork.playlistmaker.di.viewModelModule
import com.saturnnetwork.playlistmaker.main.data.ThemeRepository

class App : Application() {

    override fun onCreate() {
        super.onCreate()
        initKoin()
        val themeRepository: ThemeRepository by inject()
        val darkTheme = themeRepository.isDarkThemeEnabled()
        switchTheme(darkTheme)

    }


    fun initKoin() {
        startKoin {
            androidContext(this@App)
            modules(dataModule, repositoryModule, interactorModule, viewModelModule)
        }
    }

    fun switchTheme(darkThemeEnabled: Boolean) {

        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }
}