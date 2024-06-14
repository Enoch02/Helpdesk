package com.enoch02.helpdesk.ui.screen.authentication

import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoch02.helpdesk.data.remote.repository.auth.FirebaseAuthRepository
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import com.enoch02.helpdesk.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    private val realtimeDatabaseRepository: FirestoreRepository
) :
    ViewModel() {
    private val _registrationState = Channel<AuthState>()
    private val _loginState = Channel<AuthState>()

    val registrationState = _registrationState.receiveAsFlow()
    val loginState = _loginState.receiveAsFlow()

    var screenState by mutableStateOf(AuthScreenState.SIGN_IN)
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    //TODO: load its value from shared prefs
    var rememberMe by mutableStateOf(true)


    fun changeState(newValue: AuthScreenState) {
        screenState = newValue
    }

    fun updateName(newName: String) {
        name = newName
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
        viewModelScope.launch {
            authRepository.loginUser(email, password).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _registrationState.send(AuthState(isError = result.message))
                    }

                    is Resource.Loading -> {
                        _registrationState.send(AuthState(isLoading = true))
                    }

                    is Resource.Success -> {
                        _registrationState.send(AuthState(isSuccess = "Login Successful"))
                    }
                }
            }
        }
    }

    fun signUp() {
        viewModelScope.launch {
            authRepository.registerUser(email, password).collect { result ->
                when (result) {
                    is Resource.Error -> {
                        _registrationState.send(AuthState(isError = result.message))
                    }

                    is Resource.Loading -> {
                        _registrationState.send(AuthState(isLoading = true))
                    }

                    is Resource.Success -> {
                        realtimeDatabaseRepository.createNewUserData(
                            uid = authRepository.getUID(),
                            name = name,
                            role = "Student",
                        )
                            .onSuccess {
                                _registrationState.send(AuthState(isSuccess = "Registration Complete"))
                            }
                    }
                }
            }
        }
    }

    fun isUserLoggedIn() = authRepository.isUserLoggedIn()
}