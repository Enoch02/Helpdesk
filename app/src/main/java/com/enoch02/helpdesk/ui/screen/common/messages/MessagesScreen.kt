package com.enoch02.helpdesk.ui.screen.common.messages

import androidx.compose.material3.Scaffold
import androidx.compose.runtime.Composable
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController

@Composable
fun MessagesScreen(navController: NavController, viewModel: MessagesViewModel = hiltViewModel()) {
    Scaffold(
        topBar = {},
        content = { paddingValues ->

        }
    )
}