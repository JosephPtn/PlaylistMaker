package com.saturnnetwork.playlistmaker.medialibraries.data.db

import com.saturnnetwork.playlistmaker.medialibraries.data.db.converters.TrackDbConvertor
import com.saturnnetwork.playlistmaker.medialibraries.domain.db.TrackDBRepository
import com.saturnnetwork.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.distinctUntilChanged
import kotlinx.coroutines.flow.map

class TrackDBRepositoryImpl(
    private val appDatabase: AppDatabase,
    private val converter: TrackDbConvertor,
): TrackDBRepository {

    override suspend fun addTrackToFavorite(track: Track) {
        appDatabase.trackDao().insertTrack(converter.toEntity(track))

    }

    override suspend fun deleteFromFavorite(id: String) {
        appDatabase.trackDao().deleteTrackById(id)
    }

    override fun getAllTrackFromFavorite(): Flow<List<Track>> =
        appDatabase.trackDao()
            .getTracks()
            .map { it.map(converter::fromEntity) }
            .distinctUntilChanged()

}