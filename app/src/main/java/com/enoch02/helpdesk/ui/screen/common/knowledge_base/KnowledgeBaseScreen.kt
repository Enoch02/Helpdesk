package com.enoch02.helpdesk.ui.screen.common.knowledge_base

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.rememberScrollState
import androidx.compose.foundation.verticalScroll
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Sort
import androidx.compose.material.icons.filled.Add
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.FloatingActionButton
import androidx.compose.material3.HorizontalDivider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SearchBar
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.style.TextAlign
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.enoch02.helpdesk.data.local.model.ContentState
import com.enoch02.helpdesk.ui.screen.common.knowledge_base.component.ArticleCreationBottomSheet
import com.enoch02.helpdesk.ui.screen.common.knowledge_base.component.KnowledgeBaseListItem
import com.enoch02.helpdesk.util.STAFF_ROLE

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun KnowledgeBaseScreen(
    navController: NavController,
    viewModel: KnowledgeBaseViewModel = hiltViewModel(),
) {
    val contentState = viewModel.contentState
    val query = viewModel.query
    val searchResult = viewModel.searchResult
    val active = viewModel.searchActive
    val articles = viewModel.articles
    val showBottomSheet = viewModel.showBottomSheet
    var showArticleContent by rememberSaveable {
        mutableStateOf(false)
    }
    var currentArticleTitle by rememberSaveable {
        mutableStateOf("")
    }
    var currentArticleText by rememberSaveable {
        mutableStateOf("")
    }

    SideEffect {
        viewModel.getArticles()
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
                placeholder = { Text(text = "Title") },
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
                            IconButton(
                                onClick = { },
                                content = {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Default.Sort,
                                        contentDescription = "Sort"
                                    )
                                }
                            )
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
                                    var authorName = ""

                                    LaunchedEffect(
                                        key1 = index,
                                        block = {
                                            viewModel.getUserNameFrom(item.authorId.toString())
                                                .onSuccess { name ->
                                                    authorName = name
                                                }
                                        }
                                    )

                                    KnowledgeBaseListItem(
                                        title = item.title.toString(),
                                        author = authorName,
                                        creationDate = item.creationDate.toString(),
                                        onClick = {
                                            currentArticleTitle = item.title.toString()
                                            currentArticleText = item.content.toString()
                                            showArticleContent = true
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
        floatingActionButton = {
            if (viewModel.role == STAFF_ROLE) {
                FloatingActionButton(
                    onClick = { viewModel.showBottomSheet = true },
                    content = {
                        Icon(
                            imageVector = Icons.Default.Add,
                            contentDescription = "Add new article"
                        )
                    }
                )
            }
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
                            LazyColumn(
                                content = {
                                    items(
                                        count = articles.size,
                                        itemContent = { index ->
                                            val article = articles[index]
                                            var authorName by rememberSaveable {
                                                mutableStateOf("")
                                            }

                                            LaunchedEffect(
                                                key1 = Unit,
                                                block = {
                                                    viewModel.getUserNameFrom(article.authorId.toString())
                                                        .onSuccess { name ->
                                                            authorName = name
                                                        }
                                                }
                                            )

                                            KnowledgeBaseListItem(
                                                title = article.title.toString(),
                                                author = authorName,
                                                creationDate = article.creationDate.toString(),
                                                onClick = {
                                                    currentArticleTitle = article.title.toString()
                                                    currentArticleText = article.content.toString()
                                                    showArticleContent = true
                                                }
                                            )
                                        }
                                    )
                                },
                                modifier = Modifier.padding(paddingValues)
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

            ArticleCreationBottomSheet(
                showBottomSheet = showBottomSheet,
                onDismiss = { viewModel.showBottomSheet = false },
                onSubmit = { title, content ->
                    viewModel.createArticle(title, content)
                }
            )

            if (showArticleContent) {
                val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)

                Surface {
                    ModalBottomSheet(
                        onDismissRequest = { showArticleContent = false },
                        sheetState = sheetState,
                        content = {
                            Column(
                                modifier = Modifier.fillMaxSize(),
                                content = {
                                    Text(
                                        text = currentArticleTitle,
                                        textAlign = TextAlign.Start,
                                        fontSize = MaterialTheme.typography.headlineLarge.fontSize,
                                        fontWeight = MaterialTheme.typography.headlineLarge.fontWeight,
                                        lineHeight = MaterialTheme.typography.headlineLarge.lineHeight,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                    )

                                    HorizontalDivider(Modifier.padding(vertical = 4.dp))

                                    Text(
                                        text = currentArticleText,
                                        textAlign = TextAlign.Justify,
                                        modifier = Modifier
                                            .fillMaxWidth()
                                            .padding(8.dp)
                                            .verticalScroll(
                                                rememberScrollState()
                                            )
                                    )
                                }
                            )
                        }
                    )
                }
            }
        }
    )
}