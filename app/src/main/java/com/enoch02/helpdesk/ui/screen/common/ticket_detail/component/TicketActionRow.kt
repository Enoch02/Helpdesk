package com.enoch02.helpdesk.ui.screen.common.ticket_detail.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview

@Composable
fun TicketActionRow(modifier: Modifier) {
    // TODO: hide some buttons based on ticket state
    Row(
        horizontalArrangement = Arrangement.SpaceBetween,
        content = {
            Button(
                onClick = { /*TODO*/ },
                content = {
                    Text(text = "Close")
                }
            )

            Button(
                onClick = { /*TODO*/ },
                content = {
                    Text(text = "Reopen")
                }
            )

            Button(
                onClick = { /*TODO*/ },
                content = {
                    Text(text = "Chat")
                }
            )
        },
        modifier = modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    TicketActionRow(Modifier)
}