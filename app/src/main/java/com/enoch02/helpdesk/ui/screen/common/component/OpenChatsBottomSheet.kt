package com.enoch02.helpdesk.ui.screen.common.component

import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Card
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItem
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.enoch02.helpdesk.data.remote.model.Chat

//TODO: staff version might need a different implementation
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun OpenChatsBottomSheet(showBottomSheet: Boolean, onDismiss: () -> Unit, chats: List<Chat>) {
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

    Surface {
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = {
                    onDismiss()
                },
                sheetState = sheetState,
                content = {
                    when (chats.isEmpty()) {
                        true -> {
                            Column(
                                verticalArrangement = Arrangement.Center,
                                horizontalAlignment = Alignment.CenterHorizontally,
                                modifier = Modifier.fillMaxSize(),
                                content = {
                                    Text(text = "No Messages Found")
                                }
                            )
                        }

                        false -> {
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
                                                            // TODO: get name from ID
                                                            Text(text = "Chat with ${chats[index].members?.staffID}")
                                                        },
                                                        supportingContent = {
                                                            // TODO: get ticket subject from ticketID
                                                            Text(text = "For ${chats[index].ticketID}")
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
                        }
                    }
                }
            )
        }
    }
}