package com.enoch02.helpdesk.ui.screen.common.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxHeight
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.text.SpanStyle
import androidx.compose.ui.text.buildAnnotatedString
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.text.withStyle
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.enoch02.helpdesk.data.local.model.ChatsData
import com.enoch02.helpdesk.data.remote.model.Chat
import com.enoch02.helpdesk.ui.screen.user.home.StudentHomeViewModel
import com.enoch02.helpdesk.util.DEFAULT_DISPLAY_NAME


@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenChatsBottomSheet(
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    chats: List<Chat>,
    chatsData: List<ChatsData>,
    onChatItemClicked: (chatID: String) -> Unit,
) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Surface {
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    onDismiss()
                },
                sheetState = sheetState,
                content = {
                    when (chatsData.isEmpty()) {
                        true -> {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier
                                    .fillMaxWidth()
                                    .fillMaxHeight(0.75f),
                                content = {
                                    Text(text = "No Chats Found")
                                }
                            )
                        }

                        false -> {
                            Box(
                                content = {
                                    Card(
                                        modifier = Modifier
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                        content = {
                                            LazyColumn(
                                                content = {
                                                    items(
                                                        count = chats.size,
                                                        itemContent = { index ->
                                                            ListItem(
                                                                headlineContent = {
                                                                    Text(
                                                                        text = buildAnnotatedString {
                                                                            append("Chat with ")
                                                                            withStyle(
                                                                                SpanStyle(
                                                                                    fontWeight = FontWeight.Bold
                                                                                )
                                                                            ) {
                                                                                append(
                                                                                    chatsData[index].name
                                                                                        ?: DEFAULT_DISPLAY_NAME
                                                                                )
                                                                            }
                                                                            append(" for ticket with subject ")
                                                                            withStyle(
                                                                                SpanStyle(
                                                                                    fontWeight = FontWeight.Bold
                                                                                )
                                                                            ) {
                                                                                append(chatsData[index].ticketSubject)
                                                                            }
                                                                        },
                                                                        textAlign = TextAlign.Justify
                                                                    )
                                                                },
                                                                supportingContent = {
                                                                    Text(
                                                                        text = chatsData[index].mostRecentMessage
                                                                            ?: ""
                                                                    )
                                                                },
                                                                leadingContent = {
                                                                    val profilePic =
                                                                        chatsData[index].profilePic

                                                                    AnimatedVisibility(
                                                                        visible = profilePic == null,
                                                                        content = {
                                                                            Icon(
                                                                                imageVector = Icons.Default.AccountCircle,
                                                                                contentDescription = null
                                                                            )
                                                                        }
                                                                    )

                                                                    AnimatedVisibility(
                                                                        visible = profilePic != null,
                                                                        content = {
                                                                            AsyncImage(
                                                                                model = profilePic,
                                                                                contentDescription = null,
                                                                                contentScale = ContentScale.Crop,
                                                                                modifier = Modifier
                                                                                    .size(24.dp)
                                                                                    .clip(
                                                                                        CircleShape
                                                                                    )
                                                                            )
                                                                        }
                                                                    )
                                                                },
                                                                modifier = Modifier.clickable {
                                                                    onChatItemClicked(chats[index].chatID.toString())
                                                                }
                                                            )

                                                            if (index < chats.size - 1) {
                                                                HorizontalDivider()
                                                            }
                                                        }
                                                    )
                                                }
                                            )
                                        }
                                    )
                                },
                                modifier = Modifier.fillMaxHeight(0.75f)
                            )
                        }
                    }
                },
            )
        }
    }
}