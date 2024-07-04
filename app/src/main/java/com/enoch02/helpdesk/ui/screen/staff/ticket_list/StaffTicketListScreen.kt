package com.enoch02.helpdesk.ui.screen.staff.ticket_list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.ListItemDefaults
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.enoch02.helpdesk.data.local.model.ContentState
import com.enoch02.helpdesk.data.remote.model.Ticket
import com.enoch02.helpdesk.navigation.Screen
import com.enoch02.helpdesk.ui.screen.common.component.LoadingView
import com.enoch02.helpdesk.ui.screen.common.component.SearchBarType
import com.enoch02.helpdesk.ui.screen.common.component.SortingDialog
import com.enoch02.helpdesk.ui.screen.common.component.TicketListSearchBar
import com.enoch02.helpdesk.ui.screen.staff.ticket_list.component.StaffTicketListItem
import com.enoch02.helpdesk.ui.screen.staff.user_list.component.UserListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StaffTicketListScreen(
    navController: NavController,
    filter: String,
    viewModel: StaffTicketListViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val contentState = viewModel.contentState
    val query = viewModel.query
    val searchResult = viewModel.searchResult
    val active = viewModel.searchActive
    val tickets = viewModel.tickets.tickets

    val pullToRefreshState = rememberPullToRefreshState()
    val isRefreshing = viewModel.isRefreshing

    var reassigningTicket by remember {
        mutableStateOf(false)
    }

    var showSortingDialog by remember {
        mutableStateOf(false)
    }
    val currentSorting = viewModel.currentSorting

    LaunchedEffect(Unit) {
        viewModel.getTickets(filter)
    }

    if (pullToRefreshState.isRefreshing) {
        LaunchedEffect(true) {
            viewModel.onRefresh(filter)
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
            TicketListSearchBar(
                type = SearchBarType.STAFF,
                query = query,
                onQueryChange = {
                    viewModel.updateQuery(it)
                },
                onSearch = {
                    viewModel.updateSearchStatus(true)
                    viewModel.startSearch()
                },
                active = active,
                onActiveChange = {
                    when (it) {
                        true -> {/*TODO*/
                        }

                        false -> {
                            viewModel.clearQuery()
                        }
                    }
                },
                placeHolder = "Subject",
                onClearButtonClicked = { viewModel.updateSearchStatus(false) },
                onNavBackButtonClicked = { navController.popBackStack() },
                onSortButtonClicked = { showSortingDialog = true },
                list = searchResult,
                onResultItemClicked = {
                    navController.navigate(
                        Screen.TicketDetail.withArgs(
                            searchResult[it].uid.toString(),
                            searchResult[it].ticketID.toString()
                        )
                    )
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
                            AnimatedVisibility(
                                visible = (tickets?.size ?: 0) > 0,
                                content = {
                                    Card(
                                        modifier = Modifier
                                            .padding(paddingValues)
                                            .padding(horizontal = 8.dp, vertical = 4.dp),
                                        content = {
                                            Box(
                                                content = {
                                                    LazyColumn(
                                                        content = {
                                                            items(
                                                                count = tickets?.size ?: 0,
                                                                itemContent = { index ->
                                                                    val item = tickets?.get(index)

                                                                    if (item != null) {
                                                                        StaffTicketListItem(
                                                                            isAssignedToMe = item.staffID.toString() == viewModel.getUID(),
                                                                            ticketID = item.ticketID.toString(),
                                                                            subject = item.subject.toString(),
                                                                            priority = item.priority.toString(),
                                                                            status = item.status.toString(),
                                                                            onClick = {
                                                                                navController.navigate(
                                                                                    Screen.TicketDetail.withArgs(
                                                                                        item.uid.toString(),
                                                                                        item.ticketID.toString()
                                                                                    )
                                                                                )
                                                                            },
                                                                            onAssignToSelf = {
                                                                                viewModel.assignTicketToSelf(
                                                                                    context,
                                                                                    ticket = item
                                                                                )
                                                                                viewModel.isRefreshing =
                                                                                    true
                                                                            },
                                                                            onReassign = {
                                                                                viewModel.getUsers()
                                                                                reassigningTicket =
                                                                                    true
                                                                                viewModel.isRefreshing =
                                                                                    true
                                                                                viewModel.selectedTicketIndex =
                                                                                    index
                                                                            }
                                                                        )

                                                                        if (index < tickets.size - 1) {
                                                                            HorizontalDivider()
                                                                        }
                                                                    }
                                                                }
                                                            )
                                                        },
                                                        modifier = Modifier.nestedScroll(
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
                                            )
                                        }
                                    )
                                }
                            )

                            AnimatedVisibility(
                                visible = (tickets?.size ?: 0) == 0,
                                content = {
                                    Column(
                                        verticalArrangement = Arrangement.Center,
                                        horizontalAlignment = Alignment.CenterHorizontally,
                                        content = {
                                            Text(text = "No ticket found")
                                        },
                                        modifier = Modifier.fillMaxSize()
                                    )
                                }
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

            // staff selection dialog
            AnimatedVisibility(
                visible = reassigningTicket,
                content = {
                    val staff = viewModel.staff
                    val profilePics = viewModel.profilePictures
                    val selectedStaffIndex = viewModel.selectedStaffIndex

                    AlertDialog(
                        onDismissRequest = { reassigningTicket = false },
                        title = { Text(text = "Assign To") },
                        confirmButton = {
                            TextButton(
                                onClick = {
                                    reassigningTicket = false
                                    viewModel.assignTicket(
                                        context = context,
                                        sid = staff[selectedStaffIndex].userID.toString(),
                                        ticket = tickets?.get(viewModel.selectedTicketIndex)
                                            ?: Ticket()
                                    )
                                },
                                content = {
                                    Text(text = "OK")
                                }
                            )
                        },
                        dismissButton = {
                            TextButton(
                                onClick = { reassigningTicket = false },
                                content = {
                                    Text(text = "Cancel")
                                }
                            )
                        },
                        text = {
                            when (viewModel.reassignDialogContentState) {
                                ContentState.LOADING -> {
                                    LoadingView(modifier = Modifier.size(200.dp))
                                }

                                ContentState.COMPLETED -> {
                                    Card {
                                        LazyColumn(
                                            content = {
                                                items(
                                                    count = staff.size,
                                                    itemContent = { index ->
                                                        val bg = if (selectedStaffIndex == index) {
                                                            MaterialTheme.colorScheme.primary.copy(0.5f)
                                                        } else {
                                                            Color.Transparent
                                                        }

                                                        UserListItem(
                                                            showMenu = false,
                                                            profilePicUri = if (profilePics.isNotEmpty()) profilePics[index] else null,
                                                            name = staff[index].displayName.toString(),
                                                            role = staff[index].role.toString(),
                                                            email = staff[index].email.toString(),
                                                            isUserMe = false,
                                                            colors = ListItemDefaults.colors(
                                                                containerColor = bg
                                                            ),
                                                            modifier = Modifier
                                                                .clickable {
                                                                    viewModel.updateSelectedStaff(
                                                                        index
                                                                    )
                                                                }
                                                        )
                                                    }
                                                )

                                                item {
                                                    if (staff.isEmpty()) {
                                                        Text(text = "No staff available")
                                                    }
                                                }
                                            }
                                        )
                                    }
                                }

                                ContentState.FAILURE -> {

                                }
                            }
                        }
                    )
                }
            )
        }
    )

    SortingDialog(
        showSortingDialog = showSortingDialog,
        onDismiss = { showSortingDialog = false },
        onConfirm = {
            showSortingDialog = false
        },
        currentSorting = currentSorting,
        onSelectionChange = {
            showSortingDialog = false
            viewModel.updateCurrentSorting(it)
            viewModel.getTickets(filter)
        }
    )
}