package com.enoch02.helpdesk.ui.screen.student.ticket_list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
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
import androidx.compose.material3.Divider
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import com.enoch02.helpdesk.navigation.Screen
import com.enoch02.helpdesk.ui.screen.student.ticket_list.component.TicketListItem

//TODO: show only recent tickets or all open tickets depending on where the user is
// navigating from!
@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun TicketListScreen(navController: NavController, viewModel: TicketListViewModel = viewModel()) {
    val query = viewModel.query
    val active = viewModel.searchActive

    Scaffold(
        topBar = {
            SearchBar(
                query = query,
                onQueryChange = {
                    viewModel.updateQuery(it)
                },
                onSearch = {
                    viewModel.updateSearchStatus(true)
                },
                active = active,
                onActiveChange = {},
                leadingIcon = {
                    AnimatedContent(targetState = active, label = "") {
                        when (it) {
                            true -> {
                                IconButton(
                                    onClick = { viewModel.updateSearchStatus(false) },
                                    content = {
                                        Icon(
                                            imageVector = Icons.Default.Clear,
                                            contentDescription = "Clear searchbar"
                                        )
                                    }
                                )
                            }

                            false -> {
                                Icon(imageVector = Icons.Default.Search, contentDescription = null)
                            }
                        }
                    }
                },
                trailingIcon = {
                    AnimatedVisibility(
                        visible = !active,
                        content = {
                            Row {
                                IconButton(
                                    onClick = { /*TODO*/ },
                                    content = {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Default.Sort,
                                            contentDescription = "Sort Tickets"
                                        )
                                    }
                                )

                                IconButton(
                                    onClick = { /*TODO*/ },
                                    content = {
                                        Icon(
                                            imageVector = Icons.Default.FilterList,
                                            contentDescription = "Filter Tickets"
                                        )
                                    }
                                )
                            }
                        }
                    )
                },
                content = {
                    LazyColumn(
                        content = {
                            item {
                                Text(text = "Demo Content")
                            }

                            items(
                                count = 10,
                                itemContent = {
                                    TicketListItem(
                                        subject = "Ticket #$it",
                                        status = "Closed",
                                        onClick = {

                                        }
                                    )

                                    if (it < 9) {
                                        Divider()
                                    }
                                }
                            )
                        },
                        modifier = Modifier.fillMaxSize()
                    )
                },
                modifier = Modifier.fillMaxWidth()
            )
        },
        content = { paddingValues ->
            Card(
                modifier = Modifier
                    .padding(paddingValues)
                    .padding(horizontal = 8.dp, vertical = 4.dp),
                content = {
                    LazyColumn(
                        content = {
                            items(
                                count = 20,
                                itemContent = {

                                    TicketListItem(
                                        subject = "Ticket #$it",
                                        status = "Closed",
                                        onClick = {
                                            navController.navigate(Screen.TicketDetail.route)
                                        }
                                    )
                                    if (it < 19) {
                                        Divider()
                                    }
                                }
                            )
                        }
                    )
                }
            )
        }
    )
}