package com.saturnnetwork.playlistmaker.search.data.network


import com.saturnnetwork.playlistmaker.search.data.NetworkClient
import com.saturnnetwork.playlistmaker.search.data.dto.Response
import com.saturnnetwork.playlistmaker.search.data.dto.SearchRequest
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.IOException

class RetrofitNetworkClient (private val itunesService: ItunesApiService) : NetworkClient {

    /*
       Проверка: если это SearchRequest, извлекаем expression (например, "Beatles").
       Вызываем метод itunesService.search(...), который возвращает Call<SearchResponse>.
       Выполняем запрос синхронно: .execute().
       Получаем SearchResponse, в котором уже есть results: ArrayList<TrackDTO>.
       Возвращаем этот объект с добавленным HTTP-кодом (resultCode).
     */
    override fun doRequest(dto: Any): Response {
        if (dto is SearchRequest) {
            return try {
                val resp = itunesService.search(dto.expression).execute()
                val body = resp.body() ?: Response()
                body.apply { resultCode = resp.code() }
            } catch (e: IOException) {
                // Ошибка сети — возвращаем Response с кодом ошибки
                Response().apply { resultCode = 500 }
            }
        } else {
            return Response().apply { resultCode = 400 }
        }
    }


}