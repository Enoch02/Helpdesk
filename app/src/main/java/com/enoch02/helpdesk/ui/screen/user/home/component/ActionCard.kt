package com.enoch02.helpdesk.ui.screen.user.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.vector.ImageVector
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ActionCard(icon: ImageVector, label: String, onClick: () -> Unit = {}) {
    Card(
        onClick = { onClick() },
        content = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Icon(imageVector = icon, contentDescription = null)

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(text = label)
                },
                modifier = Modifier.fillMaxSize()
            )
        },
        modifier = Modifier
            .size(120.dp)
            .padding(8.dp)
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    ActionCard(Icons.Default.ConfirmationNumber, label = "Open Tickets")
}