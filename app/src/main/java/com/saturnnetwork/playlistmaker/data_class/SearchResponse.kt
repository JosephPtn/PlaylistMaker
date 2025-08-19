package com.saturnnetwork.playlistmaker.data_class

import com.saturnnetwork.playlistmaker.Track

data class SearchResponse(
    val resultCount: Int,
    val results: ArrayList<Track>
)
