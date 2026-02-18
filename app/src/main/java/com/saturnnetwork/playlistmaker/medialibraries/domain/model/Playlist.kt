package com.saturnnetwork.playlistmaker.medialibraries.domain.model

import androidx.room.PrimaryKey
import java.io.Serializable

@kotlinx.serialization.Serializable
data class Playlist(
    val id: Long,
    val name: String,
    val description: String,
    val coverImagePath: String,
    var trackIds: List<String>,
    val trackCount: Int = 0
): Serializable
