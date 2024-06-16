package com.enoch02.helpdesk.ui.screen.common.chat.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun ChatBubble(content: String, owner: BubbleOwner) {
    val alignment =
        when (owner) {
            BubbleOwner.REMOTE -> {
                Alignment.CenterStart
            }

            BubbleOwner.LOCAL -> {
                Alignment.CenterEnd
            }
        }
    val textAlign =
        when (owner) {
            BubbleOwner.REMOTE -> {
                TextAlign.Start
            }

            BubbleOwner.LOCAL -> {
                TextAlign.Start
            }
        }
    val colors = CardDefaults.cardColors(
        containerColor = when (owner) {
            BubbleOwner.REMOTE -> {
                MaterialTheme.colorScheme.surfaceVariant
            }
            BubbleOwner.LOCAL -> {
                MaterialTheme.colorScheme.primary
            }
        }
    )


    Box(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        content = {
            Card(
                content = {
                    Text(
                        text = content,
                        //textAlign = textAlign,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )
                },
                colors = colors,
                modifier = Modifier.fillMaxWidth(0.7f)
            )
        },
        contentAlignment = alignment
    )
}


@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun Preview() {
    Column(modifier = Modifier.fillMaxWidth()) {
        ChatBubble(content = "Hello, World!", owner = BubbleOwner.REMOTE)
        ChatBubble(content = "Nice to meet ya!", owner = BubbleOwner.LOCAL)
    }
}