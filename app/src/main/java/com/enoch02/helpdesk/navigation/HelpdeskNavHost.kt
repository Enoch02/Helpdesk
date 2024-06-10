package com.enoch02.helpdesk.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.enoch02.helpdesk.ui.screen.authentication.AuthenticationScreen
import com.enoch02.helpdesk.ui.screen.student.create_ticket.CreateTicketScreen
import com.enoch02.helpdesk.ui.screen.student.home.StudentHomeScreen

@Composable
fun HelpdeskNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.Authentication.route,
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
        }
    )
}
