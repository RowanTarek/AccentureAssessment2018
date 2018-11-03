package com.ardev.assessment.accenture.albumsviewer

import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import kotlinx.android.synthetic.main.item_album_view.view.*

class AlbumsListAdapter(private val albumsList: List<Album>) : RecyclerView.Adapter<AlbumViewHolder>() {
    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): AlbumViewHolder {
        return AlbumViewHolder(LayoutInflater.from(parent.context).inflate(R.layout.item_album_view, parent, false))
    }

    override fun getItemCount(): Int = albumsList.size

    override fun onBindViewHolder(holder: AlbumViewHolder, position: Int) {
        val item = albumsList[position]
        holder.albumTitle.text = item.title
    }

}

class AlbumViewHolder(val view: View) : RecyclerView.ViewHolder(view) {
    val albumTitle: TextView = view.albumTitleTextView
}
