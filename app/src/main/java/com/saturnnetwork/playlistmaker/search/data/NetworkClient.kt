package com.saturnnetwork.playlistmaker.search.data

import com.saturnnetwork.playlistmaker.search.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response

}