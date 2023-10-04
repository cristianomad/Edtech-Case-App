package com.cristianomadeira.ulessontest.data.service

import androidx.media3.common.util.UnstableApi
import androidx.media3.exoplayer.offline.DownloadRequest

@UnstableApi
interface MediaDownloadManager {
    /**
     * Add a new download to the download manager.
     * @param url The url of the media to be downloaded.
     */
    fun addDownload(url: String)
}