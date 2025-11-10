package com.saturnnetwork.playlistmaker.search.data.dto

/*
   Это ответ от API — список песен (results)
   Это класс, который будет автоматически заполнен Retrofit’ом, когда получит JSON-ответ.
   Он расширяет Response, так что ты можешь к нему добавить resultCode.
 */
class SearchResponse(val results: ArrayList<TrackDTO>) : Response()