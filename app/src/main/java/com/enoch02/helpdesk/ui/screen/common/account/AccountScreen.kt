package com.enoch02.helpdesk.ui.screen.common.account

import androidx.compose.animation.AnimatedContent
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
import com.enoch02.helpdesk.ui.screen.common.account.component.Profile

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun AccountScreen(navController: NavController, viewModel: AccountViewModel = hiltViewModel()) {
    val showProfile = viewModel.showProfile
    val currentImage = viewModel.currentImage
    val displayName = viewModel.userData.displayName

    LaunchedEffect(key1 = Unit) {
        viewModel.getUserData()
        viewModel.getProfilePicture()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(
                        text = when (showProfile) {
                            true -> ""
                            false -> "Account"
                        }
                    )
                },
                navigationIcon = {
                    IconButton(
                        onClick = {
                            if (showProfile) {
                                navController.popBackStack()
                            } else {
                                viewModel.toggleShowProfile()
                            }
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
            AnimatedContent(
                targetState = showProfile,
                content = {
                    when (it) {
                        true -> {
                            Profile(
                                displayName = displayName,
                                profilePic = currentImage,
                                onDisplayNameClick = {
                                    viewModel.toggleShowProfile()
                                },
                                modifier = Modifier.padding(paddingValues)
                            )
                        }

                        false -> {
                            AccountEdit(
                                currentImage = currentImage,
                                onImageChange = { newImage -> viewModel.updateCurrentImage(newImage) },
                                currentName = displayName,
                                onNameChange = { newName -> viewModel.updateCurrentName(newName) },
                                onSaveChangesClicked = {
                                    viewModel.updateUserInfo()
                                    viewModel.toggleShowProfile()
                                },
                                modifier = Modifier.padding(paddingValues)
                            )
                        }
                    }
                },
                label = ""
            )
        }
    )
}