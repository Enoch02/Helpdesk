package com.enoch02.helpdesk.ui.screen.common.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import com.enoch02.helpdesk.util.DEFAULT_DISPLAY_NAME

@Composable
fun Header(userName: String, role: String = DEFAULT_DISPLAY_NAME, modifier: Modifier) {
    val showMessage = when (userName) {
        "User" -> false
        else -> true
    }
    Column(
        modifier = modifier,
        content = {
            AnimatedVisibility(
                visible = showMessage,
                content = {
                    Text(
                        text = "Welcome, $userName!",
                        fontSize = MaterialTheme.typography.headlineSmall.fontSize,
                        fontWeight = FontWeight.Bold
                    )
                }
            )

            if (role == DEFAULT_DISPLAY_NAME)
                Text(text = "What would you like to do?", modifier = Modifier.padding(start = 4.dp))
        }
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun Preview() {
    Header(userName = "Adesanya Enoch", modifier = Modifier.fillMaxWidth())
}