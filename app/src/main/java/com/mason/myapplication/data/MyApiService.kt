package com.mason.myapplication.data

import kotlinx.coroutines.flow.Flow
import retrofit2.Call
import retrofit2.http.Body
import retrofit2.http.GET
import retrofit2.http.POST
import retrofit2.http.Path

interface MyAPIService {
    // 測試網站      https://jsonplaceholder.typicode.com/
    // GET網址      https://jsonplaceholder.typicode.com/albums/1
    // POST網址     https://jsonplaceholder.typicode.com/albums
    // ...typicode.com/[這裡就是API的路徑]
    // 設置一個GET連線，路徑為albums/1
    // 取得的回傳資料用Albums物件接收，連線名稱取為getAlbums
    @get:GET("json?size=1000000")
    val allBikeStop: Call<List<Youbike2RealtimeItem>>

//    @GET("albums/{id}")
//    fun  // 用{}表示路徑參數，@Path會將參數帶入至該位置
//            getAlbumsById(@Path("id") id: Int): Call<Youbike2RealtimeItem?>?

//    @POST("albums")
//    fun  // 用@Body表示要傳送Body資料
//            postAlbums(@Body bikeStop: Youbike2RealtimeItem?): Call<Youbike2RealtimeItem?>?
}