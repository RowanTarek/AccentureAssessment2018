package com.ardev.assessment.accenture.albumsviewer

import android.arch.lifecycle.MutableLiveData
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

interface AlbumsDataProvider {
    fun loadAlbums(): MutableLiveData<List<Album>>
}

//============================================================================================
class RemoteDataProvider : AlbumsDataProvider {
    private val remoteClient: RetrofitClient = RetrofitClient()
    override fun loadAlbums(): MutableLiveData<List<Album>> {
        val data = MutableLiveData<List<Album>>()
        remoteClient.getRemoteServices().getAlbums().enqueue(object : Callback<List<Album>> {
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

//============================================================================================
class LocalDataProvider : AlbumsDataProvider {
    private val localClient: LocalStorageClient = LocalStorageClient()

    override fun loadAlbums(): MutableLiveData<List<Album>> {
        val albumsList = Gson().fromJson<List<Album>>(localClient.getData(), object : TypeToken<List<Album>>() {}.type)
        val liveData = MutableLiveData<List<Album>>()
        liveData.value = if (albumsList != null && albumsList.isEmpty()) {
            null
        } else {
            albumsList
        }
        return liveData
    }

    fun saveAlbums(value: List<Album>?) {
        val dataAsJsonString = Gson().toJson(value)
        localClient.saveData(dataAsJsonString)
    }
}