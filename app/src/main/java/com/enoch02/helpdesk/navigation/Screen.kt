package com.enoch02.helpdesk.navigation

sealed class Screen(val route: String) {
    data object FindHome: Screen("find_home")

    data object Authentication : Screen("authentication_screen")
    data object StudentHome: Screen("student_home_screen")
    data object CreateTicket: Screen("create_ticket_screen")
    data object TicketList: Screen("ticket_list_screen")
    data object TicketDetail: Screen("ticket_detail_screen")
    data object Chat: Screen("chat_screen")
    data object Feedback: Screen("feedback_screen")
    data object Settings: Screen("settings_screen")
    data object Account: Screen("profile_screen")
    data object KnowledgeBase: Screen("knowledge_base_screen")

    /*Staff only screens*/
    data object StaffHome: Screen("staff_home_screen")
    data object StaffTicketList: Screen("staff_ticket_list")
    data object UserList: Screen("user_list")

    fun withArgs(vararg args: String): String {
        return buildString {
            append(route)
            args.forEach { arg ->
                append("/$arg")
            }
        }
    }
}
