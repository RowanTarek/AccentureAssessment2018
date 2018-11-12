package com.ardev.assessment.accenture.albumsviewer

import android.util.Log
import com.google.gson.GsonBuilder
import okhttp3.*
import okhttp3.logging.HttpLoggingInterceptor
import retrofit2.Call
import retrofit2.http.GET
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import java.io.File
import java.util.concurrent.TimeUnit
import okhttp3.OkHttpClient
import okhttp3.CacheControl


interface RemoteServicesInterface {
    @GET("/albums")
    fun getAlbums(): Call<List<Album>>
}

class RetrofitClient(private val networkChecker: NetworkUtil) {
    private val retrofit: Retrofit
    private val cachedRetrofit: Retrofit
    private val okHttpClient: OkHttpClient
    private val cachedOkHttpClient: OkHttpClient

    private val cacheSize = (5 * 1024 * 1024).toLong()  // 5M (good enough for textual only response)

    init {
        val HEADER_CACHE_CONTROL = "Cache-Control";
        val HEADER_PRAGMA = "Pragma";


        // added for network logging
        val httpInterceptor = HttpLoggingInterceptor(HttpLoggingInterceptor.Logger { message -> Log.i("RETROFIT", message) })
        httpInterceptor.level = HttpLoggingInterceptor.Level.BODY

        val httpCacheDirectory = File(networkChecker.appContext.cacheDir, "http-cache")

        val myCache = Cache(httpCacheDirectory, cacheSize)  //This is not a good thing, using the context from the networkChecker

        /* val offlineEnabledClient = OkHttpClient.Builder()
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
                     var response: Response = chain.proceed(request)
                     if (networkChecker.isConnected()) {
                         val cacheControl : CacheControl = CacheControl.Builder()
                                 .maxAge( 2, TimeUnit.MINUTES )
                                 .build();
                         response = response.newBuilder()
 //                                .removeHeader("Pragma")
                                 .header("Cache-Control", cacheControl.toString())
                                 .build();
                     } else {

                         val cacheControl = CacheControl.Builder()
                                 .maxStale(7, TimeUnit.DAYS)
                                 .build()

                         response = response.newBuilder()
 //                                .removeHeader("Pragma")
                                 .header("Cache-Control", cacheControl.toString())//"public, only-if-cached, max-stale=" + 7 * 24 * 60 * 60)
                                 .build();
                     }
                     System.out.println("---> network: " + response.networkResponse());
                     System.out.println("---> cache: " + response.cacheResponse());

                     response.newBuilder()
                             .removeHeader("Pragma")
                             .header("Cache-Control", "max-age=6")
                             .build()

 //                    response
                 }
                 .build()
 */

        val cacheInterceptor = Interceptor { chain ->

            val response: Response = chain.proceed(chain.request())
            val cacheControl: CacheControl =
                    if (networkChecker.isConnected()) {
                        CacheControl.Builder().maxAge(0, TimeUnit.SECONDS).build()
                    } else {
                        CacheControl.Builder().maxStale(7, TimeUnit.DAYS).build()
                    }
            response.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .header(HEADER_CACHE_CONTROL, cacheControl.toString())
                    .build();
        }//end cacheInterceptor

        val offlineInterceptor = Interceptor { chain ->
            val request: Request = chain.request()
            if (!networkChecker.isConnected()) {
                val cacheControl: CacheControl =
                        CacheControl.Builder().maxStale(7, TimeUnit.DAYS).build()

                request.newBuilder()
                        .removeHeader(HEADER_PRAGMA)
                        .removeHeader(HEADER_CACHE_CONTROL)
                        .cacheControl(cacheControl)
                        .build()
            }
            chain.proceed(request)
        }

        val forcedOfflineInterceptor = Interceptor { chain ->
            val request: Request = chain.request()
            val cacheControl: CacheControl = CacheControl.Builder().maxStale(7, TimeUnit.DAYS)
                    .build()
            request.newBuilder()
                    .removeHeader(HEADER_PRAGMA)
                    .removeHeader(HEADER_CACHE_CONTROL)
                    .cacheControl(cacheControl)
                    .build();
            chain.proceed(request)
        }//end forcedOfflineInterceptor

        // Add all interceptors you want (headers, URL, logging)
        okHttpClient = OkHttpClient.Builder()
                .addInterceptor(offlineInterceptor)
                .addNetworkInterceptor(cacheInterceptor)
                .addInterceptor(httpInterceptor)
                .cache(myCache)
                .build()

        cachedOkHttpClient = OkHttpClient.Builder()
                .addInterceptor(forcedOfflineInterceptor)
                .addInterceptor(httpInterceptor)
                .cache(myCache)
                .build()


        retrofit = Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                // Add your adapter factory to handler Errors
                .client(okHttpClient)
                .build()

        cachedRetrofit = Retrofit.Builder()
                .baseUrl("https://jsonplaceholder.typicode.com/")
                .addConverterFactory(GsonConverterFactory.create(GsonBuilder().create()))
                .client(cachedOkHttpClient)
                .build()


    } //end init

    fun getRetrofit() = retrofit
    fun getCachedRetrofit() = cachedRetrofit
    fun getRemoteServices(retrofitObj : Retrofit) = retrofitObj.create<RemoteServicesInterface>(RemoteServicesInterface::class.java)
}