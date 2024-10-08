package com.enoch02.helpdesk.ui.screen.authentication

import android.content.Context
import android.widget.Toast
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoch02.helpdesk.data.remote.model.UserData
import com.enoch02.helpdesk.data.remote.repository.auth.FirebaseAuthRepository
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import com.enoch02.helpdesk.data.local.model.Resource
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.channels.Channel
import kotlinx.coroutines.flow.receiveAsFlow
import kotlinx.coroutines.launch
import kotlinx.coroutines.withContext
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

    var screenState by mutableStateOf(AuthScreenState.SIGN_IN)
    var name by mutableStateOf("")

    var email by mutableStateOf("")
    var password by mutableStateOf("")

    var userData by mutableStateOf<UserData?>(null)

    //TODO: load its value from shared prefs or firebase?
    var rememberMe by mutableStateOf(true)

    init {
        if (authRepository.isUserLoggedIn()) {
            getUserData()
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
                        _loginState.send(AuthState(isSuccess = "Login Successful"))
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
                            userData = UserData(
                                userID = authRepository.getUID(),
                                displayName = name,
                                role = "User",
                                email = authRepository.getMail()
                            )
                        )
                            .onSuccess {
                                _registrationState.send(AuthState(isSuccess = "Registration Complete"))
                            }
                    }
                }
            }
        }
    }

    private fun getUserData() {
        viewModelScope.launch {
            firestoreRepository.getUserData(uid = authRepository.getUID())
                .onSuccess {
                    userData = it
                }
        }
    }

    fun resetPassword(context: Context, email: String) {
        viewModelScope.launch(Dispatchers.IO) {
            authRepository.resetPassword(email)
                .onSuccess {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, "Password Reset Link sent", Toast.LENGTH_SHORT)
                            .show()
                    }
                }
                .onFailure {
                    withContext(Dispatchers.Main) {
                        Toast.makeText(context, it.message, Toast.LENGTH_SHORT)
                            .show()
                    }
                }
        }
    }
}