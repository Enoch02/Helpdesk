package com.enoch02.helpdesk.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.enoch02.helpdesk.ui.screen.authentication.AuthenticationScreen
import com.enoch02.helpdesk.ui.screen.common.chat.ChatScreen
import com.enoch02.helpdesk.ui.screen.common.ticket_detail.TicketDetailScreen
import com.enoch02.helpdesk.ui.screen.student.create_ticket.CreateTicketScreen
import com.enoch02.helpdesk.ui.screen.student.home.StudentHomeScreen
import com.enoch02.helpdesk.ui.screen.student.ticket_list.TicketListScreen

@Composable
fun HelpdeskNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        /*startDestination = Screen.Authentication.route,*/
        startDestination = Screen.TicketDetail.route,
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

            composable(Screen.TicketList.route) {
                TicketListScreen(navController = navController)
            }

            composable(Screen.TicketDetail.route) {
                TicketDetailScreen(navController = navController)
            }

            composable(Screen.Chat.route) {
                ChatScreen(navController = navController)
            }
        }
    )
}
