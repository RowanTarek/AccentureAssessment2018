package com.ardev.assessment.accenture.albumsviewer

import android.app.Application
import android.arch.lifecycle.AndroidViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData


class AlbumsViewModel(application: Application) : AndroidViewModel(application) {
    private lateinit var albums: MutableLiveData<List<Album>>
    private val albumsRepository: AlbumsRepository = AlbumsRepository(NetworkUtil(application))


    fun getAlbums(): LiveData<List<Album>> {
        if (!::albums.isInitialized) {
            albums = MutableLiveData()
            albums = albumsRepository.getAlbums()
        }
        return albums
    }
}