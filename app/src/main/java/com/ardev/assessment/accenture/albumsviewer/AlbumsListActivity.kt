package com.ardev.assessment.accenture.albumsviewer

import android.support.v7.app.AppCompatActivity
import android.os.Bundle

class AlbumsListActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_albums_list)
        supportFragmentManager.beginTransaction().replace(R.id.mainContentContainer, AlbumsListFragment.newInstance()).commit()
    }
}
