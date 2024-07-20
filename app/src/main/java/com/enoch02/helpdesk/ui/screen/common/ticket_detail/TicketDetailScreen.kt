package com.enoch02.helpdesk.ui.screen.common.ticket_detail

import android.net.Uri
import android.widget.Toast
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FabPosition
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.enoch02.helpdesk.data.local.model.ContentState
import com.enoch02.helpdesk.navigation.Screen
import com.enoch02.helpdesk.ui.screen.common.ticket_detail.component.AttachmentViewer
import com.enoch02.helpdesk.ui.screen.common.ticket_detail.component.TicketActionRow
import com.enoch02.helpdesk.ui.screen.common.ticket_detail.component.TicketDetailCard
import com.enoch02.helpdesk.util.DEFAULT_DISPLAY_NAME
import com.enoch02.helpdesk.util.STAFF_ROLE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailScreen(
    navController: NavController,
    uid: String,
    tid: String,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    ),
    viewModel: TicketDetailViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val contentState = viewModel.contentState

    val userData = viewModel.userData
    val subject = viewModel.subject
    val category = viewModel.category
    val priority = viewModel.priority
    val status = viewModel.status
    val creationDate = viewModel.creationDate
    val description = viewModel.description
    val staffID = viewModel.staffID
    val assignedTo = viewModel.assignedTo
    val createdBy = viewModel.createdBy
    val attachments = viewModel.attachments

    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    var bottomSheetContent by remember {
        mutableStateOf(Uri.EMPTY)
    }

    SideEffect {
        viewModel.getTicket(uid, tid)
        viewModel.getUserData()
    }

    Scaffold(
        topBar = {
            LargeTopAppBar(
                title = {
                    Text(
                        text = "Ticket $tid",
                        overflow = TextOverflow.Ellipsis,
                        maxLines = 1
                    )
                },
                scrollBehavior = scrollBehavior,
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null
                            )
                        }
                    )
                },
                actions = {
                    // TODO: should ticket editing be allowed?
                    /*IconButton(
                        onClick = { *//*TODO*//* },
                        content = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null
                            )
                        }
                    )*/
                }
            )
        },
        content = { paddingValues ->
            AnimatedContent(
                targetState = contentState,
                label = "",
                content = {
                    when (it) {
                        ContentState.LOADING -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                                content = {
                                    CircularProgressIndicator()
                                }
                            )
                        }

                        ContentState.COMPLETED -> {
                            LazyColumn(
                                content = {
                                    item {
                                        TicketDetailCard(
                                            label = "Subject",
                                            value = subject,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }

                                    item {
                                        TicketDetailCard(
                                            label = "Category",
                                            value = category,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }

                                    item {
                                        TicketDetailCard(
                                            label = "Priority",
                                            value = priority,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }

                                    item {
                                        TicketDetailCard(
                                            label = "Status",
                                            value = status,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }

                                    item {
                                        TicketDetailCard(
                                            label = "Creation Date",
                                            value = creationDate,
                                            modifier = Modifier.padding(vertical = 8.dp)
                                        )
                                    }

                                    item {
                                        if (assignedTo != DEFAULT_DISPLAY_NAME) {
                                            TicketDetailCard(
                                                label = "Assigned To",
                                                value = assignedTo,
                                                modifier = Modifier.padding(vertical = 8.dp)
                                            )
                                        }
                                    }


                                    item {
                                        if (userData.role.toString() == STAFF_ROLE) {
                                            TicketDetailCard(
                                                label = "Created By",
                                                value = createdBy,
                                                modifier = Modifier.padding(vertical = 8.dp)
                                            )
                                        }
                                    }

                                    item {
                                        if (description.isNotEmpty()) {
                                            TicketDetailCard(
                                                label = "Description",
                                                value = description,
                                                modifier = Modifier.padding(vertical = 8.dp)
                                            )
                                        }
                                    }

                                    item {
                                        AttachmentViewer(
                                            attachments = attachments,
                                            onAttachmentViewed = { index ->
                                                bottomSheetContent = attachments[index]
                                                showBottomSheet = true
                                            }
                                        )
                                    }
                                },
                                modifier = Modifier
                                    .padding(paddingValues)
                                    .padding(horizontal = 8.dp)
                            )
                        }

                        ContentState.FAILURE -> {
                            Box(
                                modifier = Modifier.fillMaxSize(),
                                contentAlignment = Alignment.Center,
                                content = {
                                    Text(
                                        text = "An error has occurred. \n ${viewModel.errorMessage}",
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(12.dp)
                                    )
                                }
                            )
                        }
                    }
                }
            )
        },
        bottomBar = {
            TicketActionRow(
                status = status,
                onCloseClicked = {
                    viewModel.closeTicket(
                        uid = uid,
                        tid = tid,
                        onSuccess = {
                            viewModel.getTicket(uid, tid)
                        }
                    )
                },
                onReopenClicked = {
                    viewModel.reopenTicket(
                        uid = uid,
                        tid = tid,
                        onSuccess = {
                            viewModel.getTicket(uid, tid)
                        }
                    )
                },
                modifier = Modifier.padding(8.dp)
            )
        },
        floatingActionButton = {
            AnimatedVisibility(
                visible = contentState == ContentState.COMPLETED,
                content = {
                    FloatingActionButton(
                        onClick = {
                            if (staffID.isBlank()) {
                                Toast.makeText(
                                    context,
                                    "Ticket has not been assigned",
                                    Toast.LENGTH_SHORT
                                ).show()
                            } else {
                                when (viewModel.chatID.isBlank()) {
                                    true -> {
                                        Toast.makeText(
                                            context,
                                            "Preparing chat window",
                                            Toast.LENGTH_SHORT
                                        ).show()

                                        viewModel.startNewChat(
                                            uid = uid,
                                            tid = tid,
                                            staffID = staffID
                                        )
                                    }

                                    false -> {
                                        navController.navigate(
                                            Screen.Chat.withArgs(viewModel.chatID)
                                        )
                                    }
                                }
                            }
                        },
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Filled.Message,
                                contentDescription = null
                            )
                        }
                    )
                }
            )
        },
        floatingActionButtonPosition = if (attachments.isEmpty()) FabPosition.End else FabPosition.Center,
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    )

    if (showBottomSheet) {
        ModalBottomSheet(
            onDismissRequest = {
                showBottomSheet = false
                bottomSheetContent = Uri.EMPTY
            },
            sheetState = sheetState,
            content = {
                AsyncImage(
                    model = bottomSheetContent,
                    contentDescription = null,
                    contentScale = ContentScale.FillBounds,
                    modifier = Modifier
                        .padding(horizontal = 8.dp)
                        .align(Alignment.CenterHorizontally)
                )
            }
        )
    }

    if (viewModel.navigateToChatScreen) {
        viewModel.navigateToChatScreen = false
        navController.navigate(Screen.Chat.withArgs(viewModel.chatID))
    }
}