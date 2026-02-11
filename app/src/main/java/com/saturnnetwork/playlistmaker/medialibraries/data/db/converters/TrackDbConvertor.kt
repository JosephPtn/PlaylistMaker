package com.saturnnetwork.playlistmaker.medialibraries.data.db.converters

import com.saturnnetwork.playlistmaker.medialibraries.data.db.entity.TrackEntity
import com.saturnnetwork.playlistmaker.search.domain.models.Track

class TrackDbConvertor {

    fun toEntity(track: Track): TrackEntity {
        return TrackEntity(
            trackId = track.trackId,
            artworkUrl100 = track.artworkUrl100,
            trackName = track.trackName,
            artistName = track.artistName,
            collectionName = track.collectionName,
            releaseDate = track.releaseDate,
            primaryGenreName = track.primaryGenreName,
            country = track.country,
            trackTime = track.trackTime,
            previewUrl = track.previewUrl
        )
    }

    fun fromEntity(entity: TrackEntity): Track {
        return Track(
            trackId = entity.trackId,
            artworkUrl100 = entity.artworkUrl100,
            trackName = entity.trackName,
            artistName = entity.artistName,
            collectionName = entity.collectionName,
            releaseDate = entity.releaseDate,
            primaryGenreName = entity.primaryGenreName,
            country = entity.country,
            trackTime = entity.trackTime,
            previewUrl = entity.previewUrl
        )
    }
}