package com.ardev.assessment.accenture.albumsviewer

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.Cache
import okhttp3.CacheControl
import okhttp3.OkHttpClient
import okhttp3.Response
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

class RetrofitClient(private val networkChecker: NetworkUtil) {
    private val retrofit: Retrofit
    private val cacheSize = (5 * 1024 * 1024).toLong()  // 5M (good enough for textual only response)

    init {
        // added for network logging
        val httpInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.i("RETROFIT", message) })
        httpInterceptor.level = HttpLoggingInterceptor.Level.BODY


        val myCache = Cache(networkChecker.appContext.cacheDir, cacheSize)  //This is not a good thing, using the context from the networkChecker

        val offlineEnabledClient = OkHttpClient.Builder()
                .connectTimeout(100, TimeUnit.SECONDS)
                .readTimeout(100, TimeUnit.SECONDS)
                .addInterceptor(httpInterceptor)
                .cache(myCache)
                .addInterceptor { chain ->
                    var request = chain.request()
                    request = if (networkChecker.isConnected()) {
                        request.newBuilder().cacheControl(CacheControl.FORCE_NETWORK)
//                                .header("Cache-Control", "public, max-age=" + 5)
                                .build() //5 seconds caching if connected
                    } else {
                        request.newBuilder().cacheControl(CacheControl.FORCE_CACHE)
                                //.header("Cache-Control", "public, only-if-cached, max-stale=" + 60 * 60 * 24 * 7)
                                .build() //7 days cache
                    }
                    val response: Response = chain.proceed(request)

                    System.out.println("---> network: " + response.networkResponse());
                    System.out.println("---> cache: " + response.cacheResponse());

                    response
                }
                .build()

        retrofit = Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .client(offlineEnabledClient)
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .build()
    }

    fun getRemoteServices() = retrofit.create<RemoteServicesInterface>(RemoteServicesInterface::class.java)
}