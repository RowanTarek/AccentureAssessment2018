package com.ardev.assessment.accenture.albumsviewer

import android.arch.lifecycle.MutableLiveData

class AlbumsRepository(networkUtil: NetworkUtil) {
    private val albumsDataProvider = RemoteDataProvider(networkUtil)
    fun getAlbums(): MutableLiveData<List<Album>> {
        return albumsDataProvider.loadAlbums()
    }
}