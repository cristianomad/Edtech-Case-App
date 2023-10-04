package com.cristianomadeira.ulessontest.ui.util

import android.content.Context
import android.content.ContextWrapper
import androidx.activity.ComponentActivity
import androidx.core.view.WindowCompat
import androidx.core.view.WindowInsetsCompat
import androidx.core.view.WindowInsetsControllerCompat
import androidx.media3.common.util.UnstableApi
import androidx.media3.datasource.cache.Cache
import androidx.media3.exoplayer.offline.DownloadRequest

@UnstableApi
fun DownloadRequest.isCached(cache: Cache): Boolean =
    cache.getCachedSpans(id).find { it.key == id }?.isCached ?: false

fun Context.getActivity(): ComponentActivity? = when (this) {
    is ComponentActivity -> this
    is ContextWrapper -> baseContext.getActivity()
    else -> null
}

fun Context.displaySystemUi(visible: Boolean) {
    val activity = getActivity() ?: return
    val window = activity.window ?: return

    WindowCompat.setDecorFitsSystemWindows(window, visible)
    WindowInsetsControllerCompat(window, window.decorView).let { controller ->
        val systemBars = WindowInsetsCompat.Type.systemBars()

        if (visible) {
            controller.show(systemBars)
        } else {
            controller.hide(systemBars)
            controller.systemBarsBehavior =
                WindowInsetsControllerCompat.BEHAVIOR_SHOW_TRANSIENT_BARS_BY_SWIPE
        }
    }
}
