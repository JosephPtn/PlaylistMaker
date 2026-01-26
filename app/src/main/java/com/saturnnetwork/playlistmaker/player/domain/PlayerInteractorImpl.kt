package com.saturnnetwork.playlistmaker.player.domain

import com.saturnnetwork.playlistmaker.player.domain.PlayerRepository
import com.saturnnetwork.playlistmaker.player.domain.PlayerInteractor

class PlayerInteractorImpl(private val repository: PlayerRepository) : PlayerInteractor {
    override fun preparePlayer(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        repository.prepare(url, onPrepared, onCompletion)
    }

    override fun play() = repository.play()
    override fun pause() = repository.pause()
    override fun release() = repository.release()
    override fun isPlaying() = repository.isPlaying()
    override fun getCurrentPosition() = repository.getCurrentPosition()
}