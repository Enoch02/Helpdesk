package com.enoch02.helpdesk.ui.screen.common.ticket_detail.component

import android.net.Uri
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun AttachmentViewer(
    attachments: List<Uri>,
    onAttachmentViewed: (index: Int) -> Unit,
    modifier: Modifier = Modifier,
) {
    if (attachments.isNotEmpty()) {
        Card(
            content = {
                Text(
                    text = "Attachments",
                    fontSize = MaterialTheme.typography.headlineMedium.fontSize,
                    fontWeight = FontWeight.Bold,
                    modifier = Modifier.padding(horizontal = 8.dp)
                )

                Column(
                    content = {
                        attachments.forEachIndexed { index, attachment ->
                            ListItem(
                                leadingContent = {
                                    AsyncImage(
                                        model = attachment,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier.size(40.dp)
                                    )
                                },
                                headlineContent = { Text(text = "Attachment $index") },
                                trailingContent = {
                                    Button(
                                        onClick = { onAttachmentViewed(index) },
                                        content = {
                                            Text(text = "View")
                                        }
                                    )
                                }
                            )
                        }
                    },
                    modifier = Modifier
                        .padding(4.dp)
                        .clip(RoundedCornerShape(8.dp))
                )
            },
            modifier = modifier.fillMaxWidth()
        )
    }
}