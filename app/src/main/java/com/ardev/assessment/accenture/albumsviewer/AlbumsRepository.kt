package com.ardev.assessment.accenture.albumsviewer

import android.arch.lifecycle.MutableLiveData

class AlbumsRepository {
    val albumsDataProvider = RemoteDataProvider()
    fun getAlbums() : MutableLiveData<List<Album>> {
        return albumsDataProvider.loadAlbums()
    }
}