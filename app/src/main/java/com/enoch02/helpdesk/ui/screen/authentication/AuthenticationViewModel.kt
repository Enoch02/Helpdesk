package com.enoch02.helpdesk.ui.screen.authentication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel

//@HiltViewModel
class AuthenticationViewModel : ViewModel() {
    var screenState by mutableStateOf(AuthScreenState.SIGN_IN)
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    //TODO: laod its value from shared prefs
    var rememberMe by mutableStateOf(true)


    fun changeState(newValue: AuthScreenState) {
        screenState = newValue
    }

    fun updateEmail(newEmail: String) {
        email = newEmail
    }

    fun updatePassword(newPassword: String) {
        password = newPassword
    }

    fun updateRememberMe(new: Boolean) {
        rememberMe = new
    }

    //TODO: create a function that validates inputs
    fun signIn() {

    }

    fun signUp() {

    }
}