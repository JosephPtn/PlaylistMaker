package com.saturnnetwork.playlistmaker.medialibraries.data.db.dao

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.saturnnetwork.playlistmaker.medialibraries.data.db.entity.PlaylistEntity
import com.saturnnetwork.playlistmaker.medialibraries.domain.model.Playlist
import kotlinx.coroutines.flow.Flow

@Dao
interface PlaylistDao {
    @Insert(onConflict = OnConflictStrategy.REPLACE)

    suspend fun insertPlaylist(playlist: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    fun getPlaylists(): Flow<List<PlaylistEntity>>

    // // @Query с Flow без suspend
    @Query("SELECT trackIds FROM playlist_table WHERE id = :playlistId")
    fun getTracksId(playlistId: Long): Flow<List<String>>

    // @Query без Flow с suspend
    @Query("UPDATE playlist_table SET trackIds = :trackIds WHERE id = :playlistId")
    suspend fun updateTracksId(playlistId: Long, trackIds: String)

    @Query("""
    UPDATE playlist_table 
    SET trackIds = :trackIds,
        trackCount = :trackCount
    WHERE id = :playlistId
    """)
    suspend fun insertTracksId(trackIds: String, playlistId: Long, trackCount: Int)

    @Query("SELECT * FROM playlist_table WHERE id = :playlistId")
    fun getPlaylistById(playlistId: Long): Flow<PlaylistEntity>

}