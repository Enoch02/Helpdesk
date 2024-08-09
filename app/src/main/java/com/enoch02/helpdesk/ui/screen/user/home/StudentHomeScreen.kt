package com.enoch02.helpdesk.ui.screen.user.home

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyVerticalGrid
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.HelpCenter
import androidx.compose.material.icons.automirrored.filled.Message
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ConfirmationNumber
import androidx.compose.material.icons.filled.Create
import androidx.compose.material.icons.filled.History
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material.icons.filled.Payments
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.enoch02.helpdesk.navigation.Screen
import com.enoch02.helpdesk.ui.screen.common.component.AboutAppDialog
import com.enoch02.helpdesk.ui.screen.common.component.Header
import com.enoch02.helpdesk.ui.screen.common.component.OpenChatsBottomSheet
import com.enoch02.helpdesk.ui.screen.user.home.component.ActionCard
import com.enoch02.helpdesk.util.DEFAULT_DISPLAY_NAME
import com.enoch02.helpdesk.util.openUrlInBrowser
import com.enoch02.helpdesk.util.restartActivity

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun StudentHomeScreen(
    navController: NavController,
    viewModel: StudentHomeViewModel = hiltViewModel(),
) {
    val context = LocalContext.current
    val profilePicture = viewModel.profilePicture
    val userData = viewModel.userData
    val chats = viewModel.chats
    val chatsData = viewModel.allChatsData


    var showBottomSheet by remember {
        mutableStateOf(false)
    }
    var showAboutDialog by rememberSaveable {
        mutableStateOf(false)
    }

    SideEffect {
        viewModel.getProfilePicture()
        viewModel.getUserData()
        viewModel.getChats()
        viewModel.getChatData()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = { /*TODO*/ },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.navigate(Screen.Account.route) },
                        content = {
                            AnimatedVisibility(
                                visible = profilePicture == null,
                                content = {
                                    Icon(
                                        imageVector = Icons.Default.AccountCircle,
                                        contentDescription = null
                                    )
                                }
                            )

                            AnimatedVisibility(
                                visible = profilePicture != null,
                                content = {
                                    AsyncImage(
                                        model = profilePicture,
                                        contentDescription = null,
                                        contentScale = ContentScale.Crop,
                                        modifier = Modifier
                                            .size(24.dp)
                                            .clip(CircleShape)
                                    )
                                }
                            )
                        }
                    )
                },
                actions = {
                    var showDropDown by remember { mutableStateOf(false) }

                    IconButton(
                        onClick = { showDropDown = true },
                        content = {
                            Icon(imageVector = Icons.Default.MoreVert, contentDescription = null)
                        }
                    )

                    AnimatedVisibility(
                        visible = showDropDown,
                        content = {
                            DropdownMenu(
                                expanded = showDropDown,
                                onDismissRequest = { showDropDown = false },
                                content = {
                                    DropdownMenuItem(
                                        text = { Text("About App") },
                                        onClick = {
                                            showDropDown = false
                                            showAboutDialog = true
                                        }
                                    )

                                    DropdownMenuItem(
                                        text = { Text(text = "Sign Out") },
                                        onClick = {
                                            showDropDown = false
                                            viewModel.signOut()
                                            restartActivity(context = context)
                                        }
                                    )
                                }
                            )
                        }
                    )
                }
            )
        },
        content = { paddingValues ->
            Column(
                content = {
                    Header(
                        userName = userData.displayName ?: DEFAULT_DISPLAY_NAME,
                        modifier = Modifier.fillMaxWidth()
                    )

                    Spacer(modifier = Modifier.height(20.dp))

                    LazyVerticalGrid(
                        columns = GridCells.Fixed(2),
                        content = {
                            item {
                                ActionCard(
                                    Icons.Default.Create,
                                    label = "Create New Ticket",
                                    onClick = {
                                        navController.navigate(Screen.CreateTicket.route)
                                    }
                                )
                            }

                            item {
                                ActionCard(
                                    Icons.Default.ConfirmationNumber,
                                    label = "View Open Tickets",
                                    onClick = {
                                        navController.navigate(Screen.TicketList.withArgs("Open"))
                                    }
                                )
                            }

                            item {
                                ActionCard(
                                    Icons.AutoMirrored.Filled.HelpCenter,
                                    label = "Knowledge Base",
                                    onClick = {
                                        navController.navigate(Screen.KnowledgeBase.route)
                                    }
                                )
                            }

                            item {
                                ActionCard(
                                    Icons.Default.History,
                                    label = "All Tickets",
                                    onClick = {
                                        navController.navigate(Screen.TicketList.withArgs("all"))
                                    }
                                )
                            }

                            item {
                                ActionCard(
                                    icon = Icons.AutoMirrored.Filled.Message,
                                    label = "View Chats",
                                    onClick = {
                                        showBottomSheet = true
                                    }
                                )
                            }

                            item {
                                ActionCard(
                                    icon = Icons.Default.Payments,
                                    label = "Generate Invoice",
                                    onClick = {
                                        openUrlInBrowser(
                                            context = context,
                                            url = "https://payments.oouagoiwoye.edu.ng/new-transaction.php"
                                        )
                                    }
                                )
                            }
                        }
                    )
                },
                modifier = Modifier
                    .fillMaxSize()
                    .padding(paddingValues)
                    .padding(8.dp)
            )

            OpenChatsBottomSheet(
                showBottomSheet = showBottomSheet,
                onDismiss = { showBottomSheet = false },
                chats = chats,
                chatsData = chatsData,
                onChatItemClicked = { chatID ->
                    navController.navigate(
                        Screen.Chat.withArgs(chatID)
                    )
                }
            )

            AboutAppDialog(
                showDialog = showAboutDialog,
                onConfirm = {
                    showAboutDialog = false
                }
            )
        }
    )
}