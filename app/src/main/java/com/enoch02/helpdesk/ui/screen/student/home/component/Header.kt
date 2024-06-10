package com.enoch02.helpdesk.ui.screen.student.home.component

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

@Composable
fun Header(userName: String = "User", modifier: Modifier) {
    Column(
        modifier = modifier,
        content = {
            Text(
                text = "Welcome, $userName!",
                fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                fontWeight = FontWeight.Bold
            )

            Text(text = "What would you like to do?", modifier = Modifier.padding(start = 4.dp))
        }
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun Preview() {
    Header(modifier = Modifier.fillMaxWidth())
}