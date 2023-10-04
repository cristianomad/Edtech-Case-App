package com.cristianomadeira.ulessontest.data.service

import android.app.Notification
import android.content.Context
import android.net.Uri
import androidx.core.app.NotificationCompat
import androidx.media3.common.util.NotificationUtil.setNotification
import androidx.media3.common.util.UnstableApi
import androidx.media3.common.util.Util
import androidx.media3.exoplayer.offline.Download
import androidx.media3.exoplayer.offline.DownloadManager
import androidx.media3.exoplayer.offline.DownloadNotificationHelper
import androidx.media3.exoplayer.offline.DownloadService
import androidx.media3.exoplayer.scheduler.PlatformScheduler
import androidx.media3.exoplayer.scheduler.Scheduler
import com.cristianomadeira.ulessontest.R
import com.cristianomadeira.ulessontest.data.service.MediaDownloadUtil.DOWNLOAD_NOTIFICATION_CHANNEL_ID

/**
 * Download service implementation.
 * Adapted from: https://github.com/androidx/media/blob/release/demos/main/src/main/java/androidx/media3/demo/main/DemoDownloadService.java
 */
@UnstableApi
class MediaDownloadService : DownloadService(
    FOREGROUND_NOTIFICATION_ID,
    DEFAULT_FOREGROUND_NOTIFICATION_UPDATE_INTERVAL,
    DOWNLOAD_NOTIFICATION_CHANNEL_ID,
    R.string.exo_download_notification_channel_name,
    0
) {

    companion object {
        private const val JOB_ID = 1
        private const val FOREGROUND_NOTIFICATION_ID = 1
    }

    override fun getDownloadManager(): DownloadManager =
        MediaDownloadUtil.getDownloadManager(applicationContext).apply {
            val downloadNotificationHelper =
                MediaDownloadUtil.getDownloadNotificationHelper(applicationContext)

            addListener(
                TerminalStateNotificationHelper(
                    applicationContext,
                    downloadNotificationHelper,
                    FOREGROUND_NOTIFICATION_ID + 1
                )
            )
        }

    override fun getScheduler(): Scheduler? {
        return if (Util.SDK_INT >= 21) PlatformScheduler(applicationContext, JOB_ID) else null
    }

    override fun getForegroundNotification(
        downloads: List<Download>,
        notMetRequirements: Int
    ): Notification {
        return MediaDownloadUtil.getDownloadNotificationHelper(applicationContext)
            .buildProgressNotification(
                this,
                R.drawable.cloud_download,
                null,
                null,
                downloads,
                notMetRequirements
            )
    }

    private class TerminalStateNotificationHelper(
        private val context: Context,
        private val notificationHelper: DownloadNotificationHelper,
        private var nextNotificationId: Int
    ) : DownloadManager.Listener {

        override fun onDownloadChanged(
            downloadManager: DownloadManager,
            download: Download,
            finalException: Exception?
        ) {
            val filename = Uri.parse(download.request.toMediaItem().mediaId)
                .pathSegments.lastOrNull()

            val notification: Notification
            when (download.state) {
                Download.STATE_COMPLETED -> {
                    notification = notificationHelper.buildDownloadCompletedNotification(
                        context,
                        R.drawable.cloud_done,
                        null,
                        filename
                    )
                }

                Download.STATE_FAILED -> {
                    notification = notificationHelper.buildDownloadFailedNotification(
                        context,
                        R.drawable.cloud_off,
                        null,
                        filename
                    )
                }

                else -> return
            }

            NotificationCompat.Builder(context, notification)
                .setChannelId(DOWNLOAD_NOTIFICATION_CHANNEL_ID)
                .setPriority(NotificationCompat.PRIORITY_LOW)
                .build()
                .also { notificationManager ->
                    setNotification(context, nextNotificationId++, notificationManager)
                }
        }
    }
}