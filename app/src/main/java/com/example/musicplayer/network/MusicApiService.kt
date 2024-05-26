package com.example.musicplayer.network

import com.jakewharton.retrofit2.converter.kotlinx.serialization.asConverterFactory
import kotlinx.serialization.json.Json
import okhttp3.MediaType.Companion.toMediaType
import retrofit2.Retrofit
import retrofit2.http.GET

private const val BASE_URL = "https://storage.googleapis.com/uamp/"

private val contentType = "application/json".toMediaType()

private val retrofit: Retrofit = Retrofit.Builder()
    .addConverterFactory(Json.asConverterFactory(contentType))
    .baseUrl(BASE_URL)
    .build()

interface MusicApiService {
    @GET("catalog.json")
    suspend fun getPlaylist(): NetworkPlaylist
}

object MusicAPI {
    val retrofitService : MusicApiService by lazy {
        retrofit.create(MusicApiService::class.java)
    }
}
