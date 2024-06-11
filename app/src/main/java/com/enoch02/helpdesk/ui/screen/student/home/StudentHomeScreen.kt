package com.enoch02.helpdesk.ui.screen.student.home

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpCenter
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.enoch02.helpdesk.navigation.Screen
import com.enoch02.helpdesk.ui.screen.student.home.component.ActionCard
import com.enoch02.helpdesk.ui.screen.student.home.component.Header

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentHomeScreen(
    navController: NavController,
    viewModel: StudentHomeViewModel = viewModel()
) {
    Scaffold(
        topBar = {
            TopAppBar(
                title = { /*TODO*/ },
                navigationIcon = {
                    IconButton(
                        onClick = { /*TODO*/ },
                        content = {
                            Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
                        }
                    )
                },
                actions = {
                    IconButton(
                        onClick = { /*TODO*/ },
                        content = {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                        }
                    )
                }
            )
        },
        content = { paddingValues ->
            Column(
                content = {
                    Header(modifier = Modifier.fillMaxWidth())

                    Spacer(modifier = Modifier.height(20.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        content = {
                            item {
                                ActionCard(
                                    Icons.Default.Create,
                                    label = "Create New Ticket",
                                    onClick = {
                                        navController.navigate(Screen.CreateTicket.route)
                                    }
                                )
                            }

                            item {
                                ActionCard(
                                    Icons.Default.ConfirmationNumber,
                                    label = "Open Tickets",
                                    onClick = {
                                        navController.navigate(Screen.TicketList.route)
                                    }
                                )
                            }

                            item {
                                ActionCard(
                                    Icons.AutoMirrored.Filled.HelpCenter,
                                    label = "FAQs",
                                    onClick = {

                                    }
                                )
                            }

                            item {
                                ActionCard(
                                    Icons.Default.History,
                                    label = "Recent Tickets",
                                    onClick = {
                                        navController.navigate(Screen.TicketList.route)
                                    }
                                )
                            }
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(8.dp)
            )
        }
    )
}