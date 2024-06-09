package com.enoch02.helpdesk.navigation

import androidx.compose.runtime.Composable
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.enoch02.helpdesk.ui.screen.authentication.AuthenticationScreen

@Composable
fun HelpdeskNavHost(navController: NavHostController = rememberNavController()) {
    NavHost(
        navController = navController,
        startDestination = Screen.AuthenticationScreen.route,
        builder = {
            composable(Screen.AuthenticationScreen.route) {
                AuthenticationScreen(navController=navController)
            }
        }
    )
}
