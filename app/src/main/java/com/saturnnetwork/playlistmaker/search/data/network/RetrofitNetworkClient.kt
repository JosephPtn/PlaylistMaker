package com.saturnnetwork.playlistmaker.search.data.network


import com.saturnnetwork.playlistmaker.search.data.NetworkClient
import com.saturnnetwork.playlistmaker.search.data.dto.Response
import com.saturnnetwork.playlistmaker.search.data.dto.SearchRequest
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
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
    override suspend fun doRequest(dto: Any): Response {
        return if (dto is SearchRequest) {
            withContext(Dispatchers.IO) {
                try {
                    val response = itunesService.search(dto.expression)
                    response.apply { resultCode = 200 }
                } catch (e: Throwable) {
                    Response().apply { resultCode = 500 }
                }
            }
        } else {
            Response().apply { resultCode = 400 }
        }
    }

}