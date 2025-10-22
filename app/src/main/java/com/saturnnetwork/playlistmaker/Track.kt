package com.saturnnetwork.playlistmaker

import com.google.gson.annotations.SerializedName
import android.os.Parcelable
import kotlinx.parcelize.Parcelize

@Parcelize
data class Track (
    var trackName: String,
    var artistName: String,
    @SerializedName("trackTimeMillis") var trackTime: Long,
    var artworkUrl100: String,
    var trackId: String,
    var collectionName: String,
    val releaseDate: String?,
    var primaryGenreName: String,
    var country: String,
    var previewUrl: String,
) : Parcelable