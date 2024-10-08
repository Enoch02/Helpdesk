package com.enoch02.helpdesk.ui.screen.common.chat.component

import android.net.Uri
import androidx.compose.foundation.background
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
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.enoch02.helpdesk.R
import com.enoch02.helpdesk.util.formatTime
import com.enoch02.helpdesk.util.getCurrentDateTime

@Composable
fun ImageBubble(content: Uri?, owner: BubbleOwner, sentAt: String) {
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
                        contentDescription = null,
                        placeholder = painterResource(id = R.drawable.placeholder)
                    )

                    Text(
                        text = sentAt,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.5f))
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

@Composable
fun ImageBubble(content: Uri?, text: String, owner: BubbleOwner, sentAt: String) {
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
                        contentDescription = null,
                        placeholder = painterResource(id = R.drawable.placeholder)
                    )

                    Text(
                        text = text,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.5f))
                            .padding(8.dp)
                    )

                    Text(
                        text = sentAt,
                        textAlign = TextAlign.End,
                        modifier = Modifier
                            .fillMaxWidth()
                            .background(Color.Black.copy(alpha = 0.5f))
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
        ImageBubble(
            content = Uri.EMPTY,
            owner = BubbleOwner.REMOTE,
            sentAt = formatTime(getCurrentDateTime())
        )
        ImageBubble(
            content = Uri.EMPTY,
            owner = BubbleOwner.LOCAL,
            sentAt = formatTime(getCurrentDateTime())
        )
        ImageBubble(
            content = Uri.EMPTY,
            text = "Hello World",
            owner = BubbleOwner.LOCAL,
            sentAt = formatTime(getCurrentDateTime())
        )
    }
}