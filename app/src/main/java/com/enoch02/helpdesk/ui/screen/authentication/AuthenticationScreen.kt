package com.enoch02.helpdesk.ui.screen.authentication

import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.enoch02.helpdesk.ui.screen.authentication.component.BasicAuthForm
import com.enoch02.helpdesk.ui.screen.authentication.component.LabeledCheckBox

@Composable
fun AuthenticationScreen(
    navController: NavController,
    viewModel: AuthenticationViewModel = hiltViewModel()
) {
    val email = viewModel.email
    val password = viewModel.password
    val state = viewModel.screenState
    val rememberMe = viewModel.rememberMe
    var headerText by rememberSaveable {
        mutableStateOf("")
    }

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        content = {
            BasicAuthForm(
                headerText = when (viewModel.screenState) {
                    AuthScreenState.SIGN_IN -> {
                        "Sign in to your account"
                    }

                    AuthScreenState.SIGN_UP -> {
                        "Sign up for an account"
                    }
                },
                email = email,
                password = password,
                onEmailChange = { viewModel.updateEmail(it) },
                onPasswordChange = { viewModel.updatePassword(it) },
                onDone = {
                    when (viewModel.screenState) {
                        AuthScreenState.SIGN_IN -> {
                            viewModel.signIn()
                        }

                        AuthScreenState.SIGN_UP -> {
                            viewModel.signUp()
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            AnimatedVisibility(
                visible = state == AuthScreenState.SIGN_IN,
                content = {
                    Spacer(modifier = Modifier.height(8.dp))

                    Row(
                        modifier = Modifier.fillMaxWidth(),
                        verticalAlignment = Alignment.CenterVertically,
                        horizontalArrangement = Arrangement.SpaceBetween,
                        content = {
                            LabeledCheckBox(
                                label = "Remember Me",
                                checked = rememberMe,
                                onCheckChanged = { viewModel.updateRememberMe(it) },
                                modifier = Modifier
                            )

                            TextButton(
                                onClick = { /*TODO*/ },
                                content = {
                                    Text(text = "Reset password")
                                }
                            )
                        }
                    )
                }
            )

            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    when (viewModel.screenState) {
                        AuthScreenState.SIGN_IN -> {
                            viewModel.signIn()
                        }

                        AuthScreenState.SIGN_UP -> {
                            viewModel.signUp()
                        }
                    }
                },
                content = {
                    when (viewModel.screenState) {
                        AuthScreenState.SIGN_IN -> {
                            Text(text = "Sign In")
                        }

                        AuthScreenState.SIGN_UP -> {
                            Text(text = "Sign Up")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )

            TextButton(
                onClick = {
                    when (viewModel.screenState) {
                        AuthScreenState.SIGN_IN -> {
                            viewModel.changeState(AuthScreenState.SIGN_UP)
                        }

                        AuthScreenState.SIGN_UP -> {
                            viewModel.changeState(AuthScreenState.SIGN_IN)
                        }
                    }
                },
                content = {
                    when (viewModel.screenState) {
                        AuthScreenState.SIGN_IN -> {
                            Text(text = "Create an Account")
                        }

                        AuthScreenState.SIGN_UP -> {
                            Text(text = "Log into an Account")
                        }
                    }
                },
                modifier = Modifier.fillMaxWidth()
            )
        }
    )
}