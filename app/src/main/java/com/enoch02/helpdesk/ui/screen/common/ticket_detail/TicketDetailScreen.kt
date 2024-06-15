package com.enoch02.helpdesk.ui.screen.common.ticket_detail

import android.widget.Toast
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.LargeTopAppBar
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBarDefaults
import androidx.compose.material3.TopAppBarScrollBehavior
import androidx.compose.material3.rememberTopAppBarState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.style.TextOverflow
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.enoch02.helpdesk.navigation.Screen
import com.enoch02.helpdesk.ui.screen.common.ticket_detail.component.TicketActionRow
import com.enoch02.helpdesk.ui.screen.common.ticket_detail.component.TicketDetailCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailScreen(
    navController: NavController,
    uid: String,
    tid: String,
    scrollBehavior: TopAppBarScrollBehavior = TopAppBarDefaults.exitUntilCollapsedScrollBehavior(
        rememberTopAppBarState()
    ),
    viewModel: TicketDetailViewModel = hiltViewModel()
) {
    val context = LocalContext.current
    val subject = viewModel.subject
    val category = viewModel.category
    val priority = viewModel.priority
    val status = viewModel.status
    val creationDate = viewModel.creationDate
    val description = viewModel.description

    LaunchedEffect(
        key1 = uid,
        block = {
            viewModel.getTicket(uid, tid)
        }
    )

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
                    IconButton(
                        onClick = { /*TODO*/ },
                        content = {
                            Icon(
                                imageVector = Icons.Default.AttachFile,
                                contentDescription = null
                            )
                        }
                    )

                    IconButton(
                        onClick = { /*TODO*/ },
                        content = {
                            Icon(
                                imageVector = Icons.Default.Edit,
                                contentDescription = null
                            )
                        }
                    )
                }
            )
        },
        content = { paddingValues ->
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
                        TicketDetailCard(
                            label = "Description",
                            value = description,
                            modifier = Modifier.padding(vertical = 8.dp)
                        )
                    }

                    item {
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
                    }
                },
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 8.dp)
            )
        },
        floatingActionButton = {
            FloatingActionButton(
                onClick = { navController.navigate(Screen.Chat.route) },
                content = {
                    Icon(imageVector = Icons.AutoMirrored.Filled.Message, contentDescription = null)
                }
            )
        },
        modifier = Modifier.nestedScroll(scrollBehavior.nestedScrollConnection)
    )
}