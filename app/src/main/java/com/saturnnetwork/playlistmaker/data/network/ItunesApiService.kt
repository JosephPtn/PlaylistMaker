package com.saturnnetwork.playlistmaker.data.network

import com.saturnnetwork.playlistmaker.data.dto.SearchResponse
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.http.Query
//Вызываем метод search, передавая текст для поиска, например, "The Beatles".
//Retrofit формирует запрос по адресу /search?entity=song&term=The+Beatles

/*
   Здесь мы не пишем, как именно работает search() — мы просто говорим:
   "Вот есть такой метод, он будет делать GET-запрос на такой-то URL и вернёт результат."
   Retrofit умеет читать такой интерфейс и сам создаёт его реализацию:
   val itunesService = retrofit.create(ItunesApiService::class.java)
 */

interface ItunesApiService {
    @GET("/search?entity=song")
    fun search(@Query("term") text: String): Call<SearchResponse>
}