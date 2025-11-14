package com.saturnnetwork.playlistmaker.di

import com.saturnnetwork.playlistmaker.player.domain.PlayerInteractor
import com.saturnnetwork.playlistmaker.player.domain.PlayerInteractorImpl
import com.saturnnetwork.playlistmaker.player.data.PlayerRepositoryImpl

object PlayerInteractorCreator {
    fun create(): PlayerInteractor {
        val repository = PlayerRepositoryImpl()
        return PlayerInteractorImpl(repository)
    }
}
