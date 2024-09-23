package com.example.apiapp01.network

import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

// Configura y proporciona una instancia de Retrofit

object RetroFitClient {
    private const val BASE_URL = "https://jsonplaceholder.typicode.com/"

    val apiService: ApiService by lazy {
        Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ApiService::class.java)
    }
}
