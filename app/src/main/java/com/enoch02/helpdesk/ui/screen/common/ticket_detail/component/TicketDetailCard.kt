package com.enoch02.helpdesk.ui.screen.common.ticket_detail.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TicketDetailCard(label: String, value: String, modifier: Modifier = Modifier) {
    Card(
        content = {
            Text(
                text = label,
                fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                fontWeight = FontWeight.Bold,
                modifier = Modifier.padding(horizontal = 8.dp)
            )
            Text(text = value, modifier = Modifier.padding(horizontal = 8.dp, vertical = 8.dp))
        },
        modifier = modifier.fillMaxWidth()
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun Preview() {
    TicketDetailCard(
        label = "Hello World",
        value = "Something interesting",
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp)
    )
}