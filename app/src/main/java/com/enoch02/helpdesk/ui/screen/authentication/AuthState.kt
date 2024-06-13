package com.enoch02.helpdesk.ui.screen.authentication

data class AuthState(
    val isLoading: Boolean = false,
    val isSuccess: String? = "",
    val isError: String? = ""
)