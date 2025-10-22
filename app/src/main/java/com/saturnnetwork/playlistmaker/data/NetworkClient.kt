package com.saturnnetwork.playlistmaker.data

import com.saturnnetwork.playlistmaker.data.dto.Response

interface NetworkClient {
    fun doRequest(dto: Any): Response

}