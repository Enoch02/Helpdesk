package com.enoch02.helpdesk.ui.screen.common.account.component

import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Icon
import androidx.compose.material3.ListItem
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel

@Composable
fun EmailChanger(viewModel: AccountEditViewModel = hiltViewModel()) {
    val focusManager = LocalFocusManager.current
    val context = LocalContext.current
    var showDialog by remember {
        mutableStateOf(false)
    }
    var password by remember {
        mutableStateOf("")
    }
    var newEmail by remember {
        mutableStateOf("")
    }

    if (!showDialog) {
        password = ""
        newEmail = ""
    }

    ListItem(
        leadingContent = {
            Text(text = "Change Email")
        },
        headlineContent = { },
        trailingContent = {
            Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null)
        },
        tonalElevation = 30.dp,
        modifier = Modifier.clickable {
            showDialog = true
        }
    )

    AnimatedVisibility(
        visible = showDialog,
        content = {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = {
                    Text(text = "Change Email")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            viewModel.changeEmail(context, password, newEmail)
                            showDialog = false
                        },
                        content = {
                            Text(text = "OK")
                        }
                    )
                },
                dismissButton = {
                    TextButton(
                        onClick = { showDialog = false },
                        content = {
                            Text(text = "Cancel")
                        }
                    )
                },
                text = {
                    Column(
                        content = {
                            OutlinedTextField(
                                value = password,
                                onValueChange = { password = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text(text = "Password") },
                                visualTransformation = PasswordVisualTransformation(),
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Password,
                                    imeAction = ImeAction.Next
                                ),
                                keyboardActions = KeyboardActions(
                                    onNext = {
                                        focusManager.moveFocus(FocusDirection.Down)
                                    }
                                )
                            )

                            OutlinedTextField(
                                value = newEmail,
                                onValueChange = { newEmail = it },
                                modifier = Modifier.fillMaxWidth(),
                                label = { Text(text = "New Email") },
                                keyboardOptions = KeyboardOptions(
                                    keyboardType = KeyboardType.Email,
                                    imeAction = ImeAction.Done
                                ),
                                keyboardActions = KeyboardActions(
                                    onDone = {
                                        viewModel.changeEmail(context, password, newEmail)
                                    }
                                )
                            )
                        }
                    )
                }
            )
        }
    )
}