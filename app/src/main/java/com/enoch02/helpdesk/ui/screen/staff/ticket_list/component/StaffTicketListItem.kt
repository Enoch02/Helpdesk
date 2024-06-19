package com.enoch02.helpdesk.ui.screen.staff.ticket_list.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun StaffTicketListItem(
    ticketID: String,
    subject: String,
    priority: String,
    status: String,
    onClick: () -> Unit,
    onAssignToSelfItemClicked: () -> Unit,
) {
    var showMenu by remember {
        mutableStateOf(false)
    }

    ListItem(
        overlineContent = { Text(text = "ID: $ticketID") },
        headlineContent = { Text(text = subject) },
        supportingContent = { Text(text = "Priority: $priority | Status: $status") },
        trailingContent = {
            IconButton(
                onClick = { showMenu = true },
                content = {
                    Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                }
            )

            AnimatedVisibility(
                visible = showMenu,
                content = {
                    DropdownMenu(
                        expanded = showMenu,
                        onDismissRequest = { showMenu = false },
                        content = {
                            DropdownMenuItem(
                                text = { Text(text = "Assign to self") },
                                onClick = {
                                    showMenu = false
                                    onAssignToSelfItemClicked()
                                }
                            )

                            /*TODO: check if the ticket is assigned to this uid*/
                            DropdownMenuItem(
                                text = { Text(text = "Reassign Ticket") },
                                onClick = {
                                    showMenu = false
                                    onAssignToSelfItemClicked()
                                }
                            )
                        }
                    )
                }
            )
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
        status = "Open",
        onClick = {},
        onAssignToSelfItemClicked = {}
    )
}