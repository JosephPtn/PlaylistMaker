package com.saturnnetwork.playlistmaker.di

import com.saturnnetwork.playlistmaker.domain.player.PlayerInteractor
import com.saturnnetwork.playlistmaker.domain.player.PlayerInteractorImpl
import com.saturnnetwork.playlistmaker.domain.player.PlayerRepositoryImpl

object PlayerInteractorCreator {
    fun create(): PlayerInteractor {
        val repository = PlayerRepositoryImpl()
        return PlayerInteractorImpl(repository)
    }
}
