package com.saturnnetwork.playlistmaker.creator

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.saturnnetwork.playlistmaker.di.PlayerInteractorCreator
import com.saturnnetwork.playlistmaker.player.ui.PlayerViewModel


class PlayerViewModelFactory : ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(PlayerViewModel::class.java)) {
            val interactor = PlayerInteractorCreator.create()
            @Suppress("UNCHECKED_CAST")
            return PlayerViewModel(interactor) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class: $modelClass")
    }
}
