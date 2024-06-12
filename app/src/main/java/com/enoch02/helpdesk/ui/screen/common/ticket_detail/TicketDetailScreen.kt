package com.enoch02.helpdesk.ui.screen.common.ticket_detail

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.PaddingValues
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.filled.AttachFile
import androidx.compose.material.icons.filled.Edit
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.navigation.NavController
import androidx.lifecycle.viewmodel.compose.viewModel
import com.enoch02.helpdesk.ui.screen.common.ticket_detail.component.TicketActionRow
import com.enoch02.helpdesk.ui.screen.common.ticket_detail.component.TicketDetailCard

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketDetailScreen(
    navController: NavController,
    viewModel: TicketDetailViewModel = viewModel()
) {
    val subject = viewModel.subject
    val category = viewModel.category
    val priority = viewModel.priority
    val status = viewModel.status
    val creationDate = viewModel.creationDate
    val description = viewModel.description

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Ticket #xxxx") },
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

                    item { TicketActionRow(modifier = Modifier.padding(8.dp)) }
                },
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 8.dp)
            )
        }
    )
}