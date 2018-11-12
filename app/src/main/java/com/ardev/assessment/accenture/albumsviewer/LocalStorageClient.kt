package com.ardev.assessment.accenture.albumsviewer

import android.content.Context
import java.io.File

class LocalStorageClient {

    companion object {
        private const val cacheFileName = "albumsCache"
        lateinit var cacheFile: File

        fun init(context: Context) {
            cacheFile = File(context.filesDir, cacheFileName)
        }
    }

    fun getData(): String? {
        if (!cacheFile.exists() || !cacheFile.canRead() || cacheFile.length() == 0L) {
            return null
        } else {
            return cacheFile.readText()
        }
    }

    fun saveData(dataAsJsonString: String) {
        cacheFile.writeText(dataAsJsonString)
    }
}
