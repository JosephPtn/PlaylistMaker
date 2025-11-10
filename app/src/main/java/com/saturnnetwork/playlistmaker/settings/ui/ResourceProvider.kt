package com.saturnnetwork.playlistmaker.settings.ui

interface ResourceProvider {
    fun getString(id: Int): String
}