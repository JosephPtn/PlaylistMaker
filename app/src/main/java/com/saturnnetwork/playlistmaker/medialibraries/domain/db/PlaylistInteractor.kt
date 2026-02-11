package com.saturnnetwork.playlistmaker.medialibraries.domain.db

import com.saturnnetwork.playlistmaker.medialibraries.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

interface PlaylistInteractor {

    suspend fun addPlaylist(playlist: Playlist)

    fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun insertTracksId(trackIds: String, playlistId: Long)

    fun getPlaylistById(playlistId: Long): Flow<Playlist>

}