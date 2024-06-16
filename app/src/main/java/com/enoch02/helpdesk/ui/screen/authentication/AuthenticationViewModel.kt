package com.enoch02.helpdesk.ui.screen.authentication

import android.util.Log
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoch02.helpdesk.data.local.model.ContentState
import com.enoch02.helpdesk.data.remote.model.UserData
import com.enoch02.helpdesk.data.remote.repository.auth.FirebaseAuthRepository
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import com.enoch02.helpdesk.navigation.Screen
import com.enoch02.helpdesk.util.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AuthenticationViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    private val firestoreRepository: FirestoreRepository,
) :
    ViewModel() {
    private val _registrationState = Channel<AuthState>()
    private val _loginState = Channel<AuthState>()

    val registrationState = _registrationState.receiveAsFlow()
    val loginState = _loginState.receiveAsFlow()

    var userData by mutableStateOf<UserData?>(null)

    var screenState by mutableStateOf(AuthScreenState.SIGN_IN)
    var name by mutableStateOf("")
    var email by mutableStateOf("")
    var password by mutableStateOf("")

    //TODO: load its value from shared prefs or firebase?
    var rememberMe by mutableStateOf(true)

    var homeScreenContentState by mutableStateOf(ContentState.LOADING)

    init {
        if (authRepository.isUserLoggedIn()) {
            /*viewModelScope.launch {
                firestoreRepository.getUserData(uid = authRepository.getUID())
                    .onSuccess {
                        userData = it
                        Log.e("TAG", "signIn: $userData")
                    }
            }*/
            getUserData {

            }
        }
    }


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
                        _loginState.send(AuthState(isError = result.message))
                    }

                    is Resource.Loading -> {
                        _loginState.send(AuthState(isLoading = true))
                    }

                    is Resource.Success -> {
                        /*firestoreRepository.getUserData(uid = authRepository.getUID())
                            .onSuccess {
                                userData = it
                                _loginState.send(AuthState(isSuccess = "Login Successful"))
                            }*/

                        getUserData {
                            _loginState.send(AuthState(isSuccess = "Login Successful"))
                        }
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
                        firestoreRepository.createNewUserData(
                            uid = authRepository.getUID(),
                            name = name,
                            role = "User",
                        )
                            .onSuccess {
                                _registrationState.send(AuthState(isSuccess = "Registration Complete"))
                            }
                    }
                }
            }
        }
    }

    fun getUserData(onSuccess: suspend () -> Unit) {
        viewModelScope.launch {
            homeScreenContentState = ContentState.LOADING
            firestoreRepository.getUserData(uid = authRepository.getUID())
                .onSuccess {
                    userData = it
                    onSuccess()
                    homeScreenContentState = ContentState.COMPLETED
                }
        }
    }

    fun isUserLoggedIn() = authRepository.isUserLoggedIn()
}