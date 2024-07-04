package com.enoch02.helpdesk.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.enoch02.helpdesk.data.local.model.ContentState
import com.enoch02.helpdesk.data.remote.model.UserData
import com.enoch02.helpdesk.data.remote.repository.auth.FirebaseAuthRepository
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import com.enoch02.helpdesk.ui.screen.authentication.AuthenticationScreen
import com.enoch02.helpdesk.ui.screen.common.account.AccountScreen
import com.enoch02.helpdesk.ui.screen.common.chat.ChatScreen
import com.enoch02.helpdesk.ui.screen.common.knowledge_base.KnowledgeBaseScreen
import com.enoch02.helpdesk.ui.screen.common.settings.SettingsScreen
import com.enoch02.helpdesk.ui.screen.common.ticket_detail.TicketDetailScreen
import com.enoch02.helpdesk.ui.screen.staff.home.StaffHomeScreen
import com.enoch02.helpdesk.ui.screen.staff.ticket_list.StaffTicketListScreen
import com.enoch02.helpdesk.ui.screen.staff.user_list.UserListScreen
import com.enoch02.helpdesk.ui.screen.user.create_ticket.CreateTicketScreen
import com.enoch02.helpdesk.ui.screen.user.feedback.FeedbackScreen
import com.enoch02.helpdesk.ui.screen.user.home.StudentHomeScreen
import com.enoch02.helpdesk.ui.screen.user.ticket_list.TicketListScreen
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.launch
import javax.inject.Inject

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpdeskNavHost(
    navController: NavHostController = rememberNavController(),
    viewModel: NavHostViewModel = hiltViewModel(),
) {
    var startDestination by rememberSaveable {
        mutableStateOf(Screen.Authentication.route)
    }

    LaunchedEffect(
        key1 = Unit,
        block = {
            startDestination = if (!viewModel.isUserLoggedIn()) {
                viewModel.homeScreenContentState = ContentState.COMPLETED
                Screen.Authentication.route
            } else {
                viewModel.homeScreenContentState = ContentState.COMPLETED
                Screen.FindHome.route
            }
        }
    )
    AnimatedContent(targetState = viewModel.homeScreenContentState, label = "",
        content = { targetState ->
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
                    NavHost(
                        navController = navController,
                        startDestination = startDestination,
                        builder = {
                            composable(Screen.FindHome.route) {
                                FindHome(navController = navController)
                            }

                            composable(Screen.Authentication.route) {
                                AuthenticationScreen(navController = navController)
                            }

                            composable(Screen.StudentHome.route) {
                                StudentHomeScreen(navController = navController)
                            }

                            composable(Screen.CreateTicket.route) {
                                CreateTicketScreen(navController = navController)
                            }

                            composable(route = Screen.TicketList.route + "/{filter}",
                                arguments = listOf(
                                    navArgument("filter") {
                                        type = NavType.StringType
                                    }
                                ),
                                content = { entry ->
                                    TicketListScreen(
                                        filter = entry.arguments?.getString("filter").toString(),
                                        navController = navController
                                    )
                                }
                            )

                            composable(
                                route = Screen.TicketDetail.route + "/{uid}/{tid}",
                                arguments = listOf(
                                    navArgument(name = "uid") { type = NavType.StringType },
                                    navArgument(name = "tid") { type = NavType.StringType }),
                                content = { entry ->
                                    TicketDetailScreen(
                                        uid = entry.arguments?.getString("uid").toString(),
                                        tid = entry.arguments?.getString("tid").toString(),
                                        navController = navController
                                    )
                                }
                            )

                            composable(
                                Screen.Chat.route + "/{cid}",
                                arguments = listOf(navArgument("cid") {
                                    type = NavType.StringType
                                }),
                                content = { entry ->
                                    ChatScreen(
                                        chatID = entry.arguments?.getString("cid").toString(),
                                        navController = navController
                                    )
                                }
                            )

                            composable(Screen.Feedback.route) {
                                FeedbackScreen(navController = navController)
                            }

                            composable(Screen.Settings.route) {
                                SettingsScreen(navController = navController)
                            }

                            composable(Screen.Account.route) {
                                AccountScreen(navController = navController)
                            }

                            /*Staff*/
                            composable(Screen.StaffHome.route) {
                                StaffHomeScreen(navController = navController)
                            }

                            composable(
                                route = Screen.StaffTicketList.route + "/{filter}",
                                arguments = listOf(
                                    navArgument("filter") { type = NavType.StringType }
                                ),
                                content = { entry ->
                                    StaffTicketListScreen(
                                        navController = navController,
                                        filter = entry.arguments?.getString("filter").toString()
                                    )
                                },
                            )

                            composable(Screen.UserList.route) {
                                UserListScreen(navController = navController)
                            }

                            composable(Screen.KnowledgeBase.route) {
                                KnowledgeBaseScreen(navController = navController)
                            }
                        }
                    )
                }

                ContentState.FAILURE -> {

                }
            }
        }
    )

    /*NavHost(
        navController = navController,
        startDestination = startDestination,
        builder = {
            composable("find_home") {
                FindHome(navController = navController)
            }

            composable(Screen.Authentication.route) {
                AuthenticationScreen(navController = navController)
            }

            composable(Screen.StudentHome.route) {
                StudentHomeScreen(navController = navController)
            }

            composable(Screen.CreateTicket.route) {
                CreateTicketScreen(navController = navController)
            }

            composable(route = Screen.TicketList.route + "/{filter}",
                arguments = listOf(
                    navArgument("filter") {
                        type = NavType.StringType
                    }
                ),
                content = { entry ->
                    TicketListScreen(
                        filter = entry.arguments?.getString("filter").toString(),
                        navController = navController
                    )
                }
            )

            composable(
                route = Screen.TicketDetail.route + "/{uid}/{tid}",
                arguments = listOf(
                    navArgument(name = "uid") { type = NavType.StringType },
                    navArgument(name = "tid") { type = NavType.StringType }),
                content = { entry ->
                    TicketDetailScreen(
                        uid = entry.arguments?.getString("uid").toString(),
                        tid = entry.arguments?.getString("tid").toString(),
                        navController = navController
                    )
                }
            )

            composable(
                Screen.Chat.route + "/{cid}",
                arguments = listOf(navArgument("cid") {
                    type = NavType.StringType
                }),
                content = { entry ->
                    ChatScreen(
                        chatID = entry.arguments?.getString("cid").toString(),
                        navController = navController
                    )
                }
            )

            composable(Screen.Feedback.route) {
                FeedbackScreen(navController = navController)
            }

            composable(Screen.Settings.route) {
                SettingsScreen(navController = navController)
            }

            composable(Screen.Account.route) {
                AccountScreen(navController = navController)
            }

            *//*Staff*//*
            composable(Screen.StaffHome.route) {
                StaffHomeScreen(navController = navController)
            }

            composable(
                route = Screen.StaffTicketList.route + "/{filter}",
                arguments = listOf(
                    navArgument("filter") { type = NavType.StringType }
                ),
                content = { entry ->
                    StaffTicketLisScreen(
                        navController = navController,
                        filter = entry.arguments?.getString("filter").toString()
                    )
                },
            )

            composable(Screen.UserList.route) {
                UserListScreen(navController = navController)
            }

            composable(Screen.KnowledgeBase.route) {
                KnowledgeBaseScreen(navController = navController)
            }
        }
    )*/
}

@Composable
fun FindHome(navController: NavHostController, viewModel: FindHomeViewModel = hiltViewModel()) {
    AnimatedContent(targetState = viewModel.homeScreenContentState, label = "") {
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
                when (viewModel.userData.role) {
                    "Staff" -> {
                        navController.navigate(Screen.StaffHome.route) {
                            popUpTo(Screen.FindHome.route) {
                                inclusive = true
                            }
                        }
                    }

                    else -> {
                        navController.navigate(Screen.StudentHome.route)  {
                            popUpTo(Screen.FindHome.route) {
                                inclusive = true
                            }
                        }
                    }
                }
            }

            ContentState.FAILURE -> {

            }
        }
    }
}

@HiltViewModel
class FindHomeViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    private val firestoreRepository: FirestoreRepository,
) : ViewModel() {
    var homeScreenContentState by mutableStateOf(ContentState.LOADING)
    var userData by mutableStateOf(UserData())

    init {
        viewModelScope.launch {
            firestoreRepository.getUserData(authRepository.getUID())
                .onSuccess {
                    if (it != null) {
                        userData = it
                        homeScreenContentState = ContentState.COMPLETED
                    }
                }
        }
    }
}