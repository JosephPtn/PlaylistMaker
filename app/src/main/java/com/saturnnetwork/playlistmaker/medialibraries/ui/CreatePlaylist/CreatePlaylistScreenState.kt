package com.saturnnetwork.playlistmaker.medialibraries.ui.CreatePlaylist

import android.net.Uri

data class CreatePlaylistScreenState(
    val playListCover: Uri,
    val playListCoverTag: Int,
    val createBtnActive: Boolean,
    val createPlaylist: Boolean
)