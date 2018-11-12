package com.ardev.assessment.accenture.albumsviewer

import android.arch.lifecycle.MutableLiveData
import android.arch.lifecycle.Observer

class AlbumsRepository(private val networkUtil: NetworkUtil) {
    private val remoteProvider = RemoteDataProvider()
    private val localProvider = LocalDataProvider()
    fun getAlbums(): MutableLiveData<List<Album>> {
        LocalStorageClient.init(networkUtil.appContext) //TODO can be enhanced
        return if (networkUtil.isConnected()) {
            val loadedAlbums = remoteProvider.loadAlbums()
            loadedAlbums.observeForever(object : Observer<List<Album>> {
                override fun onChanged(t: List<Album>?) {
                    localProvider.saveAlbums(t)
                    loadedAlbums.removeObserver(this)
                }
            })
            loadedAlbums
        } else {
            localProvider.loadAlbums()
        }
    }
}