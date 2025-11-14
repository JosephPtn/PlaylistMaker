package com.saturnnetwork.playlistmaker.player.ui

import com.saturnnetwork.playlistmaker.player.domain.PlayerState
import com.saturnnetwork.playlistmaker.search.domain.models.Track

data class PlayerScreenState (

    val playerState: PlayerState = PlayerState.PREPARED,
    val track: Track?,
    val playbackPosition: String?


)
