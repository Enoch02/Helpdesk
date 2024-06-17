package com.enoch02.helpdesk.ui.screen.student.ticket_list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material.icons.filled.Search
import androidx.compose.material3.Card
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.enoch02.helpdesk.data.local.model.ContentState
import com.enoch02.helpdesk.navigation.Screen
import com.enoch02.helpdesk.ui.screen.common.component.TicketListSearchBar
import com.enoch02.helpdesk.ui.screen.student.ticket_list.component.TicketListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListScreen(
    navController: NavController,
    filter: String,
    viewModel: TicketListViewModel = hiltViewModel(),
) {
    val contentState = viewModel.contentState
    val query = viewModel.query
    val searchResult = viewModel.searchResult
    val active = viewModel.searchActive
    val tickets = viewModel.tickets.tickets

    /*LaunchedEffect(key1 = navController, block = { viewModel.getTickets(filter = filter) })*/
    SideEffect {
        viewModel.getTickets(filter = filter)
    }

    Scaffold(
        topBar = {
            TicketListSearchBar(
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
                onSortButtonClicked = { /*TODO*/ },
                onFilterButtonClicked = { /*TODO*/ },
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
                                            LazyColumn(
                                                content = {
                                                    items(
                                                        count = tickets?.size ?: 0,
                                                        itemContent = { index ->
                                                            val item = tickets?.get(index)

                                                            if (item != null) {
                                                                TicketListItem(
                                                                    subject = "${item.subject}",
                                                                    status = "${item.status}",
                                                                    onClick = {
                                                                        navController.navigate(
                                                                            Screen.TicketDetail.withArgs(
                                                                                item.uid.toString(),
                                                                                item.ticketID.toString()
                                                                            )
                                                                        )
                                                                    }
                                                                )

                                                                if (index < tickets.size - 1) {
                                                                    Divider()
                                                                }
                                                            }
                                                        }
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
                                            Text(text = "You do not have any tickets")
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
        }
    )
}