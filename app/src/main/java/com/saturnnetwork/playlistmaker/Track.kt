package com.saturnnetwork.playlistmaker

import com.google.gson.annotations.SerializedName

data class Track (
    var trackName: String,
    var artistName: String,
    @SerializedName("trackTimeMillis") var trackTime: Long,
    var artworkUrl100: String
)