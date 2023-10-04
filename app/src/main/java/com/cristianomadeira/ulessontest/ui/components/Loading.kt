package com.cristianomadeira.ulessontest.ui.components

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.width
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun Loading(
    modifier: Modifier = Modifier
) {
    Box {
        CircularProgressIndicator(
            modifier = modifier
                .align(Alignment.Center)
                .width(50.dp)
                .height(50.dp),
            color = Color.White
        )
    }
}