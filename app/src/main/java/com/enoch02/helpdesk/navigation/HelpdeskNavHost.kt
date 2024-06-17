package com.enoch02.helpdesk.navigation

import androidx.compose.animation.AnimatedContent
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.CircularProgressIndicator
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.runtime.Composable
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavHostController
import androidx.navigation.NavType
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import androidx.navigation.navArgument
import com.enoch02.helpdesk.data.local.model.ContentState
import com.enoch02.helpdesk.ui.screen.authentication.AuthenticationScreen
import com.enoch02.helpdesk.ui.screen.authentication.AuthenticationViewModel
import com.enoch02.helpdesk.ui.screen.common.account.AccountScreen
import com.enoch02.helpdesk.ui.screen.common.chat.ChatScreen
import com.enoch02.helpdesk.ui.screen.common.settings.SettingsScreen
import com.enoch02.helpdesk.ui.screen.common.ticket_detail.TicketDetailScreen
import com.enoch02.helpdesk.ui.screen.staff.home.StaffHomeScreen
import com.enoch02.helpdesk.ui.screen.staff.ticket_list.StaffTicketLisScreen
import com.enoch02.helpdesk.ui.screen.staff.user_list.UserListScreen
import com.enoch02.helpdesk.ui.screen.student.create_ticket.CreateTicketScreen
import com.enoch02.helpdesk.ui.screen.student.feedback.FeedbackScreen
import com.enoch02.helpdesk.ui.screen.student.home.StudentHomeScreen
import com.enoch02.helpdesk.ui.screen.student.ticket_list.TicketListScreen

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HelpdeskNavHost(
    navController: NavHostController = rememberNavController(),
    viewModel: NavHostViewModel = hiltViewModel(),
) {
    var startDestination by remember {
        mutableStateOf(Screen.Authentication.route)
    }

    SideEffect {
        startDestination = if (viewModel.isUserLoggedIn()) {
            if (viewModel.userData?.role == "Staff") {
                Screen.StaffHome.route
            } else {
                Screen.StudentHome.route
            }
        } else {
            Screen.Authentication.route
        }
    }

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
                                    StaffTicketLisScreen(
                                        navController = navController,
                                        filter = entry.arguments?.getString("filter").toString()
                                    )
                                },
                            )

                            composable(Screen.UserList.route) {
                                UserListScreen(navController = navController)
                            }
                        }
                    )
                }

                ContentState.FAILURE -> {

                }
            }
        }
    )
}