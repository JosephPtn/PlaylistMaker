package com.saturnnetwork.playlistmaker.medialibraries.domain.db

import com.saturnnetwork.playlistmaker.medialibraries.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

class PlaylistInteractorImpl(private val repository: PlaylistDBRepository): PlaylistInteractor {
    override suspend fun addPlaylist(playlist: Playlist) {
        repository.addPlaylist(playlist)
    }

    override fun getAllPlaylists(): Flow<List<Playlist>> {
        return repository.getAllPlaylists()
    }

    override suspend fun insertTracksId(trackIds: String, playlistId: Long, trackCount: Int) {
        repository.insertTracksId(trackIds, playlistId, trackCount)
    }

    override fun getPlaylistById(playlistId: Long): Flow<Playlist> {
        return repository.getPlaylistById(playlistId)
    }
}