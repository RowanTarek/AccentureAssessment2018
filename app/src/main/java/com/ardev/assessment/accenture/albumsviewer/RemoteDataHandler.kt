package com.ardev.assessment.accenture.albumsviewer

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.OkHttpClient
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import retrofit2.http.GET


interface RemoteServicesInterface {
    @GET("/albums")
    fun getAlbums(): Call<List<Album>>
}

class RetrofitClient {
    private val retrofit: Retrofit
    private val okHttpClient: OkHttpClient
    private val BASE_URL = "https://jsonplaceholder.typicode.com/"

    init {
        // added for network logging
        val httpInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.i("RETROFIT", message) })
        httpInterceptor.level = HttpLoggingInterceptor.Level.BODY

        // Add all interceptors you want (headers, URL, logging)
        okHttpClient = OkHttpClient.Builder()
                .addInterceptor(httpInterceptor)
                .build()

        retrofit = Retrofit.Builder()
                .baseUrl(BASE_URL)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .client(okHttpClient)
                .build()
    } //end init

    fun getRemoteServices() = retrofit.create<RemoteServicesInterface>(RemoteServicesInterface::class.java)!!
}