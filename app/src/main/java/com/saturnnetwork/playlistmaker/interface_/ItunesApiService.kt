package com.saturnnetwork.playlistmaker.interface_

import com.saturnnetwork.playlistmaker.data_class.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query

interface ItunesApiService {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<SearchResponse>
}