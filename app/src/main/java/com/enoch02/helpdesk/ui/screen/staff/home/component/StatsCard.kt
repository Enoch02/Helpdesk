package com.enoch02.helpdesk.ui.screen.staff.home.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun StatsCard(value: String, label: String, onClick: () -> Unit) {
    Card(
        onClick = { onClick() },
        content = {
            Column(
                verticalArrangement = Arrangement.Center,
                horizontalAlignment = Alignment.CenterHorizontally,
                content = {
                    Text(text = value)

                    Spacer(modifier = Modifier.height(4.dp))

                    Text(text = label)
                },
                modifier = Modifier.fillMaxSize()
            )
        },
        modifier = Modifier
            .size(160.dp)
            .padding(8.dp)
    )
}

@Preview
@Composable
private fun Preview() {
    StatsCard(
        value = "999",
        label = "Total tickets",
        onClick = {

        }
    )
}