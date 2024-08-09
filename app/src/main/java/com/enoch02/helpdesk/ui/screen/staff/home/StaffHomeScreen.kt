package com.enoch02.helpdesk.ui.screen.staff.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material.icons.filled.Feedback
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.enoch02.helpdesk.data.local.model.Filter
import com.enoch02.helpdesk.navigation.Screen
import com.enoch02.helpdesk.ui.screen.common.component.AboutAppDialog
import com.enoch02.helpdesk.ui.screen.common.component.Header
import com.enoch02.helpdesk.ui.screen.staff.home.component.StatsCard
import com.enoch02.helpdesk.ui.screen.user.home.component.ActionCard
import com.enoch02.helpdesk.util.STAFF_ROLE
import com.enoch02.helpdesk.util.restartActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffHomeScreen(navController: NavController, viewModel: StaffHomeViewModel = hiltViewModel()) {
    val context = LocalContext.current
    val profilePicture = viewModel.profilePicture
    val userData = viewModel.userData
    val ticketStats = viewModel.ticketStats

    val pullToRefreshState = rememberPullToRefreshState()
    val isRefreshing = viewModel.isRefreshing

    var showAboutDialog by rememberSaveable {
        mutableStateOf(false)
    }

    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.onRefresh()
        }
    }

    LaunchedEffect(isRefreshing) {
        if (isRefreshing) {
            pullToRefreshState.startRefresh()
        } else {
            pullToRefreshState.endRefresh()
        }
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
                                        text = { Text("About App") },
                                        onClick = {
                                            showDropDown = false
                                            showAboutDialog = true
                                        }
                                    )

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
            Box {
                Column(
                    content = {
                        Header(
                            userName = userData.displayName ?: "User",
                            role = STAFF_ROLE,
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

                                item {
                                    ActionCard(
                                        icon= Icons.Default.Feedback,
                                        label = "View Feedbacks",
                                        onClick = {
                                            navController.navigate(Screen.FeedbackViewer.route)
                                        }
                                    )
                                }
                            }
                        )
                    },
                    modifier = Modifier
                        .padding(paddingValues)
                        .nestedScroll(
                            pullToRefreshState.nestedScrollConnection
                        )
                )

                PullToRefreshContainer(
                    state = pullToRefreshState,
                    modifier = Modifier.align(
                        Alignment.TopCenter
                    )
                )
            }

            AboutAppDialog(
                showDialog = showAboutDialog,
                onConfirm = {
                    showAboutDialog = false
                }
            )
        }
    )
}