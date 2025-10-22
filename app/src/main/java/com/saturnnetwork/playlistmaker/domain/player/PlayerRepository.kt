package com.saturnnetwork.playlistmaker.domain.player

interface PlayerRepository {
    fun prepare(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit)
    fun play()
    fun pause()
    fun release()
    fun isPlaying(): Boolean
    fun getCurrentPosition(): Int
}
