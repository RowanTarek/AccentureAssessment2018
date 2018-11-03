package com.ardev.assessment.accenture.albumsviewer

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.Interceptor
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.util.concurrent.TimeUnit


interface RemoteServicesInterface {
    @GET("/albums")
    fun getAlbums(): Call<List<Album>>
}

class RetrofitClient {
    private val retrofit: Retrofit

    init {
        // added for network logging
        val httpInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.i("RETROFIT", message) })
        httpInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val client = OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .addInterceptor(httpInterceptor)
                .build()

        retrofit = Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .client(client)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
    }

    fun getRemoteServices() = retrofit.create<RemoteServicesInterface>(RemoteServicesInterface::class.java)
}