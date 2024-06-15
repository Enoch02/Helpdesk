package com.enoch02.helpdesk.ui.screen.common.ticket_detail.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun TicketActionRow(
    status: String,
    onCloseClicked: () -> Unit,
    onReopenClicked: () -> Unit,
    modifier: Modifier
) {
    Row(
        content = {
            Button(
                onClick = { onCloseClicked() },
                content = {
                    Text(text = "Close")
                },
                modifier = Modifier
                    .weight(0.5f)
                    .padding(horizontal = 8.dp),
                enabled = status == "Open"
            )

            Button(
                onClick = { onReopenClicked() },
                content = {
                    Text(text = "Reopen")
                },
                modifier = Modifier
                    .weight(0.5f)
                    .padding(horizontal = 8.dp),
                enabled = status == "Closed"
            )
        },
        modifier = modifier.fillMaxWidth()
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    TicketActionRow(
        status = "Closed",
        onCloseClicked = { /*TODO*/ },
        onReopenClicked = { /*TODO*/ },
        modifier = Modifier
    )
}