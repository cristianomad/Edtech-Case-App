package com.cristianomadeira.ulessontest.ui

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import com.cristianomadeira.ulessontest.ui.feature.NavGraphs
import com.cristianomadeira.ulessontest.ui.theme.ULessonTestTheme
import com.ramcosta.composedestinations.DestinationsNavHost
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContent {
            ULessonTestTheme {
                DestinationsNavHost(navGraph = NavGraphs.root)
            }
        }
    }
}