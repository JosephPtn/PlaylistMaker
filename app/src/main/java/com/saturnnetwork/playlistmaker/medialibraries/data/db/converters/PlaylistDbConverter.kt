package com.saturnnetwork.playlistmaker.medialibraries.data.db.converters

import com.saturnnetwork.playlistmaker.medialibraries.data.db.entity.PlaylistEntity
import com.saturnnetwork.playlistmaker.medialibraries.domain.model.Playlist

import kotlinx.serialization.json.Json
import kotlinx.serialization.decodeFromString
import kotlinx.serialization.encodeToString

class PlaylistDbConverter {

    fun toEntity(playlist: Playlist): PlaylistEntity {
        val trackIdsStr = Json.encodeToString(playlist.trackIds)
        return PlaylistEntity(
            id = playlist.id,
            name = playlist.name,
            description = playlist.description,
            coverImagePath = playlist.coverImagePath,
            trackIds = trackIdsStr,
            trackCount = playlist.trackCount
        )
    }

    fun fromEntity(entity: PlaylistEntity): Playlist {
        val listTrackIds = Json.decodeFromString<List<String>>(entity.trackIds)
        return Playlist(
            id = entity.id,
            name = entity.name,
            description = entity.description,
            coverImagePath = entity.coverImagePath,
            trackIds = listTrackIds,
            trackCount = entity.trackCount
        )
    }
}
