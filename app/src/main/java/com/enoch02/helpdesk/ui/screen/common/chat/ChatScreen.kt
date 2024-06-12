package com.enoch02.helpdesk.ui.screen.common.chat

import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController

@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = viewModel()
) {
    Scaffold(
        content = { paddingValues ->
            LazyColumn(
                content = {

                },
                modifier = Modifier.padding(paddingValues)
            )
        }
    )
}