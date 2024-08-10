package com.enoch02.helpdesk.ui.screen.authentication

import android.widget.Toast
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Button
import androidx.compose.material3.LinearProgressIndicator
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.collectAsState
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.alpha
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.testTag
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import com.enoch02.helpdesk.navigation.Screen
import com.enoch02.helpdesk.ui.screen.authentication.component.BasicAuthForm
import com.enoch02.helpdesk.ui.screen.authentication.component.LabeledCheckBox
import kotlinx.coroutines.launch

@Composable
fun AuthenticationScreen(
    navController: NavController,
    viewModel: AuthenticationViewModel = hiltViewModel(),
) {
    val scope = rememberCoroutineScope()
    val context = LocalContext.current

    val registrationState = viewModel.registrationState.collectAsState(initial = null)
    val loginState = viewModel.loginState.collectAsState(initial = null)

    val name = viewModel.name
    val email = viewModel.email
    val password = viewModel.password
    val state = viewModel.screenState
    val rememberMe = viewModel.rememberMe

    Column(
        modifier = Modifier
            .fillMaxSize()
            .padding(8.dp),
        verticalArrangement = Arrangement.Center,
        content = {
            AnimatedVisibility(
                visible = registrationState.value?.isLoading == true || loginState.value?.isLoading == true,
                content = {
                    LinearProgressIndicator(modifier = Modifier.fillMaxWidth())
                }
            )

            BasicAuthForm(
                acceptName = when (viewModel.screenState) {
                    AuthScreenState.SIGN_IN -> {
                        false
                    }

                    AuthScreenState.SIGN_UP -> {
                        true
                    }
                },
                headerText = when (viewModel.screenState) {
                    AuthScreenState.SIGN_IN -> {
                        "Sign in to your account"
                    }

                    AuthScreenState.SIGN_UP -> {
                        "Sign up for an account"
                    }
                },
                name = name,
                email = email,
                password = password,
                onNameChange = { viewModel.updateName(it) },
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
                            /*TODO*/
                            LabeledCheckBox(
                                label = "Remember Me",
                                checked = rememberMe,
                                onCheckChanged = { viewModel.updateRememberMe(it) },
                                modifier = Modifier.alpha(0f)
                            )

                            TextButton(
                                onClick = {
                                    if (email.isBlank()) {
                                        Toast.makeText(
                                            context,
                                            "Enter a valid email",
                                            Toast.LENGTH_SHORT
                                        ).show()
                                    } else {
                                        viewModel.resetPassword(context, email)
                                    }
                                },
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
                modifier = Modifier
                    .fillMaxWidth()
                    .testTag("SubmitButton")
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
                modifier = Modifier
                    .fillMaxWidth()
                    .imePadding()
            )
        }
    )

    LaunchedEffect(key1 = registrationState.value?.isSuccess) {
        scope.launch {
            if (registrationState.value?.isSuccess?.isNotEmpty() == true) {
                val success = registrationState.value?.isSuccess
                Toast.makeText(context, "$success", Toast.LENGTH_SHORT).show()

                navController.navigate(Screen.FindHome.route) {
                    popUpTo(Screen.Authentication.route) {
                        inclusive = true
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = registrationState.value?.isError) {
        scope.launch {
            if (registrationState.value?.isError?.isNotEmpty() == true) {
                val error = registrationState.value?.isError
                Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
            }
        }
    }

    LaunchedEffect(key1 = loginState.value?.isSuccess) {
        scope.launch {
            if (loginState.value?.isSuccess?.isNotEmpty() == true) {
                val success = loginState.value?.isSuccess
                Toast.makeText(context, "$success", Toast.LENGTH_SHORT).show()

                navController.navigate(Screen.FindHome.route) {
                    popUpTo(Screen.Authentication.route) {
                        inclusive = true
                    }
                }
            }
        }
    }

    LaunchedEffect(key1 = loginState.value?.isError) {
        scope.launch {
            if (loginState.value?.isError?.isNotEmpty() == true) {
                val error = loginState.value?.isError
                Toast.makeText(context, "$error", Toast.LENGTH_SHORT).show()
            }
        }
    }
}