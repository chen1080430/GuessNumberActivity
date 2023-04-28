package com.mason.myapplication.data

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory


class RetrofitManager {
    val myApi: MyAPIService
    var youbikeURL =
        "https://data.ntpc.gov.tw/api/datasets/010e5b15-3823-4b20-b401-b1cf000550c5/"
    //            "https://data.ntpc.gov.tw/api/datasets/010e5b15-3823-4b20-b401-b1cf000550c5/json?size=1000000"

    init {
        // 設置baseUrl即要連的網站，addConverterFactory用Gson作為資料處理Converter

        val retrofit = Retrofit.Builder()
            .baseUrl(youbikeURL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
        myApi = retrofit.create(MyAPIService::class.java)
    }

    companion object {
        // 以Singleton模式建立
        val instance = RetrofitManager()
    }
}