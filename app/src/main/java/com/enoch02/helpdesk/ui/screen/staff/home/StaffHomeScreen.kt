package com.enoch02.helpdesk.ui.screen.staff.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpCenter
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.enoch02.helpdesk.navigation.Screen
import com.enoch02.helpdesk.data.local.model.Filter
import com.enoch02.helpdesk.ui.screen.common.component.Header
import com.enoch02.helpdesk.ui.screen.staff.home.component.StatsCard
import com.enoch02.helpdesk.ui.screen.user.home.component.ActionCard
import com.enoch02.helpdesk.util.restartActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffHomeScreen(navController: NavController, viewModel: StaffHomeViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val profilePicture = viewModel.profilePicture
    val userData = viewModel.userData
    val ticketStats = viewModel.ticketStats

    SideEffect {
        viewModel.getProfilePicture()
        viewModel.getUserData()
        viewModel.getStats()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Dashboard") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate(Screen.Account.route) },
                        content = {
                            AnimatedVisibility(
                                visible = profilePicture == null,
                                content = {
                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = null
                                    )
                                }
                            )

                            AnimatedVisibility(
                                visible = profilePicture != null,
                                content = {
                                    AsyncImage(
                                        model = profilePicture,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                    )
                                }
                            )
                        }
                    )
                },
                actions = {
                    var showDropDown by remember { mutableStateOf(false) }

                    IconButton(
                        onClick = { showDropDown = true },
                        content = {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                        }
                    )

                    AnimatedVisibility(
                        visible = showDropDown,
                        content = {
                            DropdownMenu(
                                expanded = showDropDown,
                                onDismissRequest = { showDropDown = false },
                                content = {
                                    DropdownMenuItem(
                                        text = { Text(text = "Sign Out") },
                                        onClick = {
                                            showDropDown = false
                                            viewModel.signOut()
                                            restartActivity(context = context)
                                        }
                                    )
                                }
                            )
                        }
                    )
                }
            )
        },
        content = { paddingValues ->
            Column(
                content = {
                    Header(
                        userName = userData.displayName ?: "User",
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp)
                    )

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        content = {
                            item {
                                StatsCard(
                                    value = "${ticketStats.total}",
                                    label = "Total Tickets",
                                    onClick = {
                                        navController.navigate(
                                            Screen.StaffTicketList.withArgs(
                                                Filter.All.value
                                            )
                                        )
                                    }
                                )
                            }

                            item {
                                StatsCard(
                                    value = "${ticketStats.open}",
                                    label = "Open Tickets",
                                    onClick = {
                                        navController.navigate(
                                            Screen.StaffTicketList.withArgs(
                                                Filter.Open.value
                                            )
                                        )
                                    }
                                )
                            }

                            item {
                                StatsCard(
                                    value = "${ticketStats.closed}",
                                    label = "Closed Tickets",
                                    onClick = {
                                        navController.navigate(
                                            Screen.StaffTicketList.withArgs(
                                                Filter.Closed.value
                                            )
                                        )
                                    }
                                )
                            }
                            item {
                                StatsCard(
                                    value = "${ticketStats.unassigned}",
                                    label = "Unassigned Tickets",
                                    onClick = {
                                        navController.navigate(
                                            Screen.StaffTicketList.withArgs(
                                                Filter.Unassigned.value
                                            )
                                        )
                                    }
                                )
                            }

                            item {
                                StatsCard(
                                    value = "${ticketStats.users}",
                                    label = "Users",
                                    onClick = {
                                        navController.navigate(Screen.UserList.route)
                                    }
                                )
                            }

                            item {
                                StatsCard(
                                    value = "${ticketStats.assignedToMe}",
                                    label = "Assigned To Me",
                                    onClick = {
                                        navController.navigate(
                                            Screen.StaffTicketList.withArgs(
                                                Filter.AssignedToMe.value
                                            )
                                        )
                                    }
                                )
                            }

                            item {
                                ActionCard(
                                    Icons.AutoMirrored.Filled.HelpCenter,
                                    label = "Knowledge Base",
                                    onClick = {
                                        navController.navigate(Screen.KnowledgeBase.route)
                                    }
                                )
                            }
                        }
                    )
                },
                modifier = Modifier.padding(paddingValues)
            )
        }
    )
}