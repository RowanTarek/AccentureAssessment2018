package com.ardev.assessment.accenture.albumsviewer

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkInfo

class NetworkUtil (val appContext: Context){
    fun isConnected(): Boolean {
        val connectivityManager = appContext.getSystemService(Context.CONNECTIVITY_SERVICE) as ConnectivityManager
        val activeNetwork: NetworkInfo? = connectivityManager.activeNetworkInfo
        return (activeNetwork != null && activeNetwork.isConnected)
    }
}