package com.enoch02.helpdesk.ui.screen.staff.ticket_list.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.wrapContentSize
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Flag
import androidx.compose.material.icons.filled.LowPriority
import androidx.compose.material.icons.filled.PriorityHigh
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import com.enoch02.helpdesk.ui.screen.student.ticket_list.component.TicketListItem

@Composable
fun StaffTicketListItem(
    ticketID: String,
    subject: String,
    priority: String,
    status: String,
    onClick: () -> Unit,
) {
    ListItem(
        overlineContent = { Text(text = "ID: $ticketID") },
        headlineContent = { Text(text = subject) },
        supportingContent = { Text(text = "Priority: $priority | Status: $status") },
        trailingContent = {
            val icon = when (priority) {
                "LOW" -> {
                    Icons.Default.LowPriority
                }

                "MEDIUM" -> {
                    Icons.Default.Flag
                }

                "HIGH" -> {
                    Icons.Default.PriorityHigh
                }

                else -> {
                    Icons.Default.LowPriority
                }
            }

            //TODO: might remove box
            Box(modifier = Modifier
                .fillMaxHeight()
                .wrapContentSize(align = Alignment.Center)) {
                Icon(imageVector = icon, contentDescription = priority)
            }
        },
        modifier = Modifier.clickable {
            onClick()
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    StaffTicketListItem(
        ticketID = "abfksbkbsjbfdb",
        subject = "Help",
        priority = "HIGH",
        status = "Open", onClick = {

        }
    )
}