package com.enoch02.helpdesk.ui.screen.user.ticket_list.component

import androidx.compose.foundation.clickable
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TicketListItem(subject: String, status: String, onClick: () -> Unit) {
    ListItem(
        headlineContent = { Text(text = subject) },
        supportingContent = { Text(text = "Status: $status") },
        modifier = Modifier.clickable {
            onClick()
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    TicketListItem(subject = "Ticket #1000", status = "Open", onClick = {})
}