package com.enoch02.helpdesk.ui.screen.common.chat.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

//TODO: image bubble with text
//TODO: image bubble with multiple images
@Composable
fun ImageBubble(content: String, owner: BubbleOwner) {
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
                    AsyncImage(
                        model = content,
                        contentDescription = null
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
        ImageBubble(content = "", owner = BubbleOwner.REMOTE)
        ImageBubble(content = "", owner = BubbleOwner.LOCAL)
    }
}