package com.enoch02.helpdesk.ui.screen.common

import androidx.compose.foundation.layout.Box
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier

@Composable
fun LoadingView(modifier: Modifier) {
    Box(
        modifier = modifier,
        contentAlignment = Alignment.Center,
        content = {
            CircularProgressIndicator()
        }
    )
}