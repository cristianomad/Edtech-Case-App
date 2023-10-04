package com.cristianomadeira.ulessontest.data.service

import android.content.Context
import android.net.Uri
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.Cache
import androidx.media3.exoplayer.offline.DownloadRequest
import androidx.media3.exoplayer.offline.DownloadService
import com.cristianomadeira.ulessontest.ui.util.isCached
import javax.inject.Inject

@UnstableApi
class MediaDownloadManagerImpl @Inject constructor(
    private val context: Context,
    private val downloadCache: Cache
) : MediaDownloadManager {

    override fun addDownload(url: String) {
        val uri = Uri.parse(url)
        val downloadRequest = DownloadRequest.Builder(uri.toString(), uri).build()

        if (downloadRequest.isCached(downloadCache)) return

        DownloadService.sendAddDownload(
            context,
            MediaDownloadService::class.java,
            downloadRequest,
            false
        )
    }
}