package com.ardev.assessment.accenture.albumsviewer

import android.arch.lifecycle.Observer
import android.arch.lifecycle.ViewModelProviders
import android.os.Bundle
import android.support.v4.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import kotlinx.android.synthetic.main.content_recycler.*
import kotlinx.android.synthetic.main.frag_albums_list.*

class AlbumsListFragment : Fragment() {

    private lateinit var albumsViewModel: AlbumsViewModel

    companion object {
        @JvmStatic
        fun newInstance() = AlbumsListFragment()
    }

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?): View? {
        return inflater.inflate(R.layout.frag_albums_list, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        albumsViewModel = ViewModelProviders.of(this).get(AlbumsViewModel::class.java)
        loadAlbumsList()

    }

    private fun loadAlbumsList() {
        albumsViewModel.getAlbums().observe(this, Observer<List<Album>> { albumsList ->
            if (albumsList?.isNotEmpty() == true) {
                val sortedAlbumsList = albumsList.sortedWith(compareBy({ it.title }))
                val albumsListAdapter = AlbumsListAdapter(sortedAlbumsList)
                contentRecyclerView.adapter = albumsListAdapter
            } else {
                recyclerErrorMsgTextView.text = getString(R.string.noDataAvailable)
            }
            albumsLoadingProgressBar.visibility = View.GONE
        })
    }
}