package com.saturnnetwork.playlistmaker.search.data

import com.saturnnetwork.playlistmaker.search.data.dto.Response

interface NetworkClient {
    suspend fun doRequest(dto: Any): Response

}