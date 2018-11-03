package com.ardev.assessment.accenture.albumsviewer

import android.arch.lifecycle.MutableLiveData
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

public interface AlbumsDataProvider {
    fun loadAlbums(): MutableLiveData<List<Album>>
}


class RemoteDataProvider : AlbumsDataProvider {
    override fun loadAlbums(): MutableLiveData<List<Album>> {
        val data = MutableLiveData<List<Album>>()

        RetrofitClient().getRemoteServices().getAlbums().enqueue(object : Callback<List<Album>> {
            override fun onResponse(call: Call<List<Album>>, response: Response<List<Album>>) {
                data.value = response.body()
            }

            override fun onFailure(call: Call<List<Album>>, t: Throwable) {
                data.value = null
            }
            /* override fun onResponse(call: Call<Album>?, response: Response<Album>?) {
                 data.value = Resource.create(response)
             }

             override fun onFailure(call: Call<Album>?, t: Throwable?) {
                 val exception = Exception(t)
                 data.value = Resource.error(exception)
             }*/
        })

        return data
    }
}


class LocalDataProvider : AlbumsDataProvider {
    override fun loadAlbums(): MutableLiveData<List<Album>> {
        TODO("not implemented") //To change body of created functions use File | Settings | File Templates.
    }
}