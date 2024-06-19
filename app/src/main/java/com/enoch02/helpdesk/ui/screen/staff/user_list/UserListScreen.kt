package com.enoch02.helpdesk.ui.screen.staff.user_list

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
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
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Text
import androidx.compose.material3.pulltorefresh.PullToRefreshContainer
import androidx.compose.material3.pulltorefresh.rememberPullToRefreshState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.input.nestedscroll.nestedScroll
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import com.enoch02.helpdesk.data.local.model.ContentState
import com.enoch02.helpdesk.ui.screen.staff.user_list.component.UserListItem

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun UserListScreen(
    navController: NavHostController,
    viewModel: UserListViewModel = hiltViewModel(),
) {
    val contentState = viewModel.contentState
    val query = viewModel.query
    val searchResult = viewModel.searchResult
    val active = viewModel.searchActive
    val users = viewModel.users

    val pullToRefreshState = rememberPullToRefreshState()
    val isRefreshing = viewModel.isRefreshing

    SideEffect {
        viewModel.getUsers()
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
            SearchBar(
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
                        true -> {
                        }

                        false -> {
                            viewModel.clearQuery()
                        }
                    }
                },
                placeholder = { Text(text = "User") },
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
                                IconButton(
                                    onClick = { navController.popBackStack() },
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
                                    onClick = { },
                                    content = {
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Default.Sort,
                                            contentDescription = "Sort"
                                        )
                                    }
                                )

                                IconButton(
                                    onClick = { },
                                    content = {
                                        Icon(
                                            imageVector = Icons.Default.FilterList,
                                            contentDescription = "Filter"
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
                                count = searchResult.size,
                                itemContent = { index ->
                                    val item = searchResult[index]

                                    UserListItem(
                                        profilePicUrl = "", //TODO:
                                        name = item.displayName.toString(),
                                        role = item.role.toString(),
                                        email = item.email.toString(),
                                        onMenuClicked = {

                                        }
                                    )

                                    if (index < searchResult.size - 1) {
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
        },
        content = { paddingValues ->
            AnimatedContent(targetState = contentState, label = "") { targetState ->
                when (targetState) {
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
                        Box(
                            content = {
                                LazyColumn(
                                    content = {
                                        items(
                                            count = users.size,
                                            itemContent = { index ->
                                                UserListItem(
                                                    profilePicUrl = "", //TODO:
                                                    name = users[index].displayName.toString(),
                                                    role = users[index].role.toString(),
                                                    email = users[index].email.toString(),
                                                    onMenuClicked = {

                                                    }
                                                )

                                                if (index < users.size - 1) {
                                                    HorizontalDivider()
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

        }
    )
}