package com.saturnnetwork.playlistmaker.player.data

import android.media.MediaPlayer
import com.saturnnetwork.playlistmaker.player.domain.PlayerRepository

class PlayerRepositoryImpl(private var mediaPlayer: MediaPlayer?) : PlayerRepository {


    override fun prepare(url: String, onPrepared: () -> Unit, onCompletion: () -> Unit) {
        mediaPlayer?.reset()
        mediaPlayer?.apply {
            setOnPreparedListener { onPrepared() }
            setOnCompletionListener { onCompletion() }
            setDataSource(url)
            prepareAsync()
        }
    }


    override fun play() {
        mediaPlayer?.start()
    }

    override fun pause() {
        mediaPlayer?.pause()
    }

    override fun release() {
        mediaPlayer?.release()
        //mediaPlayer = null
    }

    override fun isPlaying(): Boolean {
        return mediaPlayer?.isPlaying == true
    }

    override fun getCurrentPosition(): Int {
        return mediaPlayer?.currentPosition ?: 0
    }
}