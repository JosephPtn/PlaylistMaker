package com.saturnnetwork.playlistmaker.medialibraries.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.saturnnetwork.playlistmaker.medialibraries.data.db.dao.TrackDao
import com.saturnnetwork.playlistmaker.medialibraries.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase: RoomDatabase() {

    abstract fun trackDao(): TrackDao

}