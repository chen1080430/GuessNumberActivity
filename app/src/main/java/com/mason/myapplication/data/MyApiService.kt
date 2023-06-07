package com.mason.myapplication.data

import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MyAPIService {
    @get:GET("json?size=10000")
    val allBikeStop: Call<List<Youbike2RealtimeItem>>
}