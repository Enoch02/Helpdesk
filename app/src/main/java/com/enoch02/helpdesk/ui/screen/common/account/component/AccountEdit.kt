package com.enoch02.helpdesk.ui.screen.common.account.component

import android.Manifest
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.ChevronRight
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Button
import androidx.compose.material3.Card
import androidx.compose.material3.Divider
import androidx.compose.material3.HorizontalDivider
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
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import coil.compose.AsyncImage

@Composable
fun AccountEdit(
    currentImage: Uri?,
    onImageChange: (Uri?) -> Unit,
    currentName: String?,
    onNameChange: (String) -> Unit,
    onSaveChangesClicked: () -> Unit,
    modifier: Modifier,
) {
    LazyColumn(
        content = {
            item {
                Card(
                    modifier = Modifier.padding(8.dp),
                    content = {
                        ProfilePictureEdit(
                            currentImage = currentImage,
                            onImageChange = {
                                onImageChange(it)
                            }
                        )

                        HorizontalDivider()

                        NewNameSelector(
                            currentName = currentName,
                            onNameChange = {
                                onNameChange(it)
                            }
                        )
                    }
                )
            }

            item {
                Card(
                    modifier = Modifier.padding(8.dp),
                    content = {
                        PasswordChanger()

                        HorizontalDivider()

                        EmailChanger()
                    }
                )
            }

            item {
                Spacer(modifier = Modifier.height(12.dp))
            }

            item {
                Button(
                    onClick = { onSaveChangesClicked() },
                    content = {
                        Text(text = "Save Changes")
                    },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(horizontal = 8.dp)
                )
            }
        },
        modifier = modifier.fillMaxSize()
    )
}

@Composable
private fun ProfilePictureEdit(currentImage: Uri?, onImageChange: (Uri?) -> Unit) {
    val context = LocalContext.current
    val getContent =
        rememberLauncherForActivityResult(ActivityResultContracts.GetContent()) { uri ->
            if (uri == null) {
                onImageChange(currentImage)
            } else {
                onImageChange(uri)
            }
        }
    val requestPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionStatusMap ->
            if (permissionStatusMap.values.all { true }) {
                getContent.launch("image/*")
            } else {
                Toast.makeText(
                    context,
                    "This permission is needed to select a picture",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    ListItem(
        leadingContent = {
            Text(text = "Avatar")
        },
        headlineContent = { },
        trailingContent = {
            if (currentImage != null) {
                AsyncImage(
                    model = currentImage,
                    contentDescription = null,
                    contentScale = ContentScale.Crop,
                    modifier = Modifier
                        .size(24.dp)
                        .clip(CircleShape)
                )
            } else {
                Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
            }
        },
        tonalElevation = 30.dp,
        modifier = Modifier.clickable {
            if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                requestPermission.launch(
                    arrayOf(
                        Manifest.permission.READ_MEDIA_IMAGES,
                        Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                    )
                )
            } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                requestPermission.launch(arrayOf(Manifest.permission.READ_MEDIA_IMAGES))
            } else {
                requestPermission.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
            }
        }
    )
}

@Composable
fun NewNameSelector(currentName: String?, onNameChange: (String) -> Unit) {
    var showDialog by remember {
        mutableStateOf(false)
    }
    var newName by remember {
        mutableStateOf(currentName ?: "")
    }

    ListItem(
        leadingContent = {
            Text(text = "Display Name")
        },
        headlineContent = { /*TODO*/ },
        trailingContent = {
            Row(verticalAlignment = Alignment.CenterVertically) {
                Text(text = currentName ?: "User")
                Spacer(modifier = Modifier.width(4.dp))
                Icon(imageVector = Icons.Default.ChevronRight, contentDescription = null)
            }
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
                    Text(text = "Change Name")
                },
                confirmButton = {
                    TextButton(
                        onClick = {
                            onNameChange(newName)
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
                    OutlinedTextField(
                        value = newName,
                        onValueChange = { newName = it },
                        modifier = Modifier.fillMaxWidth(),
                        keyboardOptions = KeyboardOptions(
                            keyboardType = KeyboardType.Text,
                            autoCorrect = true,
                            capitalization = KeyboardCapitalization.Words
                        ),
                        maxLines = 1
                    )
                }
            )
        }
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun ProfilePicPreview() {
    ProfilePictureEdit(currentImage = null, onImageChange = {})
}

@Preview(showSystemUi = true, showBackground = true, apiLevel = 33)
@Composable
private fun NewNameSelectorPreview() {
    NewNameSelector(
        currentName = "Enoch",
        onNameChange = {

        }
    )
}

@Preview(showSystemUi = true, showBackground = true, apiLevel = 33)
@Composable
private fun PasswordChangerPreview() {
    PasswordChanger()
}
