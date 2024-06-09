package com.enoch02.helpdesk.navigation

sealed class Screen(val route: String) {

    object AuthenticationScreen : Screen("authentication_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
