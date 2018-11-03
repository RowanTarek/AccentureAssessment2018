package com.ardev.assessment.accenture.albumsviewer

import android.arch.lifecycle.ViewModel
import android.arch.lifecycle.LiveData
import android.arch.lifecycle.MutableLiveData


class AlbumsViewModel : ViewModel() {
    private lateinit var albums: MutableLiveData<List<Album>>
    private val albumsRepository: AlbumsRepository = AlbumsRepository()

    fun getAlbums(): LiveData<List<Album>> {
        if (!::albums.isInitialized) {
            albums = MutableLiveData()
            albums = albumsRepository.getAlbums()
        }
        return albums
    }
}