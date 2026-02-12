package com.saturnnetwork.playlistmaker.medialibraries.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.saturnnetwork.playlistmaker.medialibraries.data.db.dao.PlaylistDao
import com.saturnnetwork.playlistmaker.medialibraries.data.db.dao.TrackDao
import com.saturnnetwork.playlistmaker.medialibraries.data.db.entity.PlaylistEntity
import com.saturnnetwork.playlistmaker.medialibraries.data.db.entity.TrackEntity

@Database(version = 1, entities = [PlaylistEntity::class])
abstract class PlaylistDatabase: RoomDatabase() {

    abstract fun playlistDao(): PlaylistDao

}
