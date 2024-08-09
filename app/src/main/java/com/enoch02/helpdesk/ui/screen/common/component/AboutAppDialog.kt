package com.enoch02.helpdesk.ui.screen.common.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign

@Composable
fun AboutAppDialog(showDialog: Boolean, onConfirm: () -> Unit) {
    AnimatedVisibility(visible = showDialog) {
        AlertDialog(
            onDismissRequest = { onConfirm() },
            confirmButton = {
                TextButton(
                    onClick = { onConfirm() },
                    content = {
                        Text(text = "OK")
                    }
                )
            },
            title = {
                Text(text = "About App")
            },
            text = {
                Text(
                    text = "Helpdesk mobile app designed and programmed by Adesanya Enoch 😎",
                    modifier = Modifier.fillMaxWidth()
                )
            }
        )
    }
}