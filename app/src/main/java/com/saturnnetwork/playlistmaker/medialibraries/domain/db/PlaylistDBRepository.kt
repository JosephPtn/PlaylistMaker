package com.saturnnetwork.playlistmaker.medialibraries.domain.db

import com.saturnnetwork.playlistmaker.medialibraries.domain.model.Playlist
import com.saturnnetwork.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow

interface PlaylistDBRepository {

    suspend fun addPlaylist(playlist: Playlist)

    fun getAllPlaylists(): Flow<List<Playlist>>

    suspend fun insertTracksId(trackIds: String, playlistId: Long, trackCount: Int)

    fun getPlaylistById(playlistId: Long): Flow<Playlist>
}