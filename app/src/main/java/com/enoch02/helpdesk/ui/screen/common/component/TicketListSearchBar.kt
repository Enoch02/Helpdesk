package com.enoch02.helpdesk.ui.screen.common.component

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material.icons.filled.FilterList
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.enoch02.helpdesk.data.remote.model.Ticket
import com.enoch02.helpdesk.ui.screen.staff.ticket_list.component.StaffTicketListItem
import com.enoch02.helpdesk.ui.screen.user.ticket_list.component.TicketListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun <T> TicketListSearchBar(
    type: SearchBarType,
    query: String,
    onQueryChange: (String) -> Unit,
    onSearch: (String) -> Unit,
    active: Boolean,
    onActiveChange: (Boolean) -> Unit,
    placeHolder: String,
    onClearButtonClicked: () -> Unit,
    onNavBackButtonClicked: () -> Unit,
    onSortButtonClicked: () -> Unit,
    onFilterButtonClicked: () -> Unit,
    list: SnapshotStateList<T>,
    onResultItemClicked: (index: Int) -> Unit,
    onAssignToSelf: () -> Unit = {},
) {
    SearchBar(
        query = query,
        onQueryChange = {
            onQueryChange(it)
        },
        onSearch = {
            onSearch(it)
        },
        active = active,
        onActiveChange = {
            onActiveChange(it)
        },
        placeholder = { Text(text = placeHolder) },
        leadingIcon = {
            AnimatedContent(targetState = active, label = "") {
                when (it) {
                    true -> {
                        IconButton(
                            onClick = { onClearButtonClicked() },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Clear,
                                    contentDescription = "Clear searchbar"
                                )
                            }
                        )
                    }

                    false -> {
                        IconButton(
                            onClick = { onNavBackButtonClicked() },
                            content = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Filled.ArrowBack,
                                    contentDescription = "Clear searchbar"
                                )
                            }
                        )
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
                            onClick = { onSortButtonClicked() },
                            content = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.Sort,
                                    contentDescription = "Sort Tickets"
                                )
                            }
                        )

                        IconButton(
                            onClick = { onFilterButtonClicked() },
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
                    items(
                        count = list.size,
                        itemContent = { index ->
                            val item = list[index] as Ticket

                            when (type) {
                                SearchBarType.USER -> {
                                    TicketListItem(
                                        subject = item.subject.toString(),
                                        status = item.status.toString(),
                                        onClick = {
                                            onResultItemClicked(index)
                                        }
                                    )
                                }
                                SearchBarType.STAFF -> {
                                    //TODO
                                    /*StaffTicketListItem(
                                        isAssignedToMe =
                                        ticketID = item.ticketID.toString(),
                                        subject = item.subject.toString(),
                                        priority = item.priority.toString(),
                                        status = item.status.toString(),
                                        onClick = {
                                            onResultItemClicked(index)
                                        },
                                        onAssignToSelf = {
                                            onAssignToSelf()
                                        },
                                        onReassign = {
                                            *//*TODO*//*
                                        }
                                    )*/
                                }
                            }

                            if (index < list.size - 1) {
                                HorizontalDivider()
                            }
                        }
                    )
                },
                modifier = Modifier.fillMaxSize()
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .then(if (active) Modifier else Modifier.padding(horizontal = 8.dp))
    )
}

enum class SearchBarType {
    USER,
    STAFF
}