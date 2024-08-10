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
import com.enoch02.helpdesk.util.formatTime
import com.enoch02.helpdesk.util.getCurrentDateTime

@Composable
fun ChatBubble(content: String, owner: BubbleOwner, sentAt: String) {
    val alignment =
        when (owner) {
            BubbleOwner.REMOTE -> {
                Alignment.CenterStart
            }

            BubbleOwner.LOCAL -> {
                Alignment.CenterEnd
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
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(10.dp)
                    )

                    Text(
                        text = sentAt,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(horizontal = 10.dp, vertical = 4.dp)
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
        ChatBubble(
            content = "Hello, World!",
            owner = BubbleOwner.REMOTE,
            sentAt = formatTime(getCurrentDateTime())
        )
        ChatBubble(
            content = "Nice to meet ya!",
            owner = BubbleOwner.LOCAL,
            sentAt = formatTime(getCurrentDateTime())
        )
    }
}