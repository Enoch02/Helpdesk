package com.enoch02.helpdesk.navigation

sealed class Screen(val route: String) {

    data object Authentication : Screen("authentication_screen")
    data object StudentHome: Screen("student_home_screen")
    data object CreateTicket: Screen("create_ticket_screen")
    data object TicketList: Screen("ticket_list_screen")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
