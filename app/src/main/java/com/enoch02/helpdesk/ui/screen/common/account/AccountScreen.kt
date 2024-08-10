package com.enoch02.helpdesk.ui.screen.common.account

import androidx.compose.foundation.layout.padding
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.enoch02.helpdesk.ui.screen.common.account.component.AccountEdit

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(navController: NavController, viewModel: AccountViewModel = hiltViewModel()) {
    val currentImage = viewModel.currentImage
    val displayName = viewModel.currentName

    LaunchedEffect(key1 = Unit) {
        viewModel.getUserData()
        viewModel.getProfilePicture()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = "Account")
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            navController.popBackStack()
                        },
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null
                            )
                        }
                    )
                }
            )
        },
        content = { paddingValues ->
            AccountEdit(
                currentImage = currentImage,
                onImageChange = { newImage -> viewModel.updateCurrentImage(newImage) },
                currentName = displayName,
                onNameChange = { newName -> viewModel.updateCurrentName(newName) },
                onSaveChangesClicked = {
                    viewModel.updateUserInfo()
                    viewModel.refreshProfile()
                },
                modifier = Modifier.padding(paddingValues)
            )
        }
    )
}