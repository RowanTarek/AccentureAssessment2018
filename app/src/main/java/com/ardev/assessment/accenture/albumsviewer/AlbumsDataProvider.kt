package com.ardev.assessment.accenture.albumsviewer

import android.arch.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface AlbumsDataProvider {
    fun loadAlbums(): MutableLiveData<List<Album>>
}

class RemoteDataProvider(private val networkUtil: NetworkUtil) : AlbumsDataProvider {
    private val remoteClient: RetrofitClient = RetrofitClient(networkUtil)

    override fun loadAlbums(): MutableLiveData<List<Album>> {
        val data = MutableLiveData<List<Album>>()
        val serviceHandler =
                if (networkUtil.isConnected()) {
                    remoteClient.getRemoteServices(remoteClient.getRetrofit())
                } else {
                    remoteClient.getRemoteServices(remoteClient.getCachedRetrofit())
                }
        serviceHandler.getAlbums().enqueue(object : Callback<List<Album>> {
            override fun onResponse(call: Call<List<Album>>, response: Response<List<Album>>) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<List<Album>>, t: Throwable) {
                data.value = null
            }
        })
        return data
    }
}
