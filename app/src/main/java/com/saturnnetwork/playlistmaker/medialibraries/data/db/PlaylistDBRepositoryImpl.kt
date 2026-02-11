package com.saturnnetwork.playlistmaker.medialibraries.data.db

import com.saturnnetwork.playlistmaker.medialibraries.data.db.converters.PlaylistDbConverter
import com.saturnnetwork.playlistmaker.medialibraries.data.db.entity.PlaylistEntity
import com.saturnnetwork.playlistmaker.medialibraries.domain.db.PlaylistDBRepository
import com.saturnnetwork.playlistmaker.medialibraries.domain.model.Playlist
import com.saturnnetwork.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map
import kotlin.collections.map

class PlaylistDBRepositoryImpl(private val appDatabase: PlaylistDatabase,
                               private val converter: PlaylistDbConverter
    ): PlaylistDBRepository {
    override suspend fun addPlaylist(playlist: Playlist) {
        appDatabase.playlistDao().insertPlaylist(converter.toEntity(playlist))
    }


    override fun getAllPlaylists(): Flow<List<Playlist>> =
        appDatabase.playlistDao()
            .getPlaylists()
            .map { it.map(converter::fromEntity) }
            .distinctUntilChanged()

    override suspend fun insertTracksId(trackIds: String, playlistId: Long) {
        appDatabase.playlistDao().insertTracksId(trackIds, playlistId)
    }

    override fun getPlaylistById(playlistId: Long): Flow<Playlist> =
        appDatabase.playlistDao()
            .getPlaylistById(playlistId)
            .map { converter.fromEntity(it) }
            .distinctUntilChanged()

}