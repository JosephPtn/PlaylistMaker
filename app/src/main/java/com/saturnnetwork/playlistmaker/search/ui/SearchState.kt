package com.saturnnetwork.playlistmaker.search.ui

import com.saturnnetwork.playlistmaker.search.domain.models.Track

data class SearchState (
    val tracks: ArrayList<Track>,
    val isLoading: Boolean,
    val errorMessageRes: Int? = null,
    val composition: String
)