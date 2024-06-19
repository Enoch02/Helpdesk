package com.enoch02.helpdesk.ui.screen.staff.user_list.component

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun UserListItem(
    profilePicUri: Uri?,
    name: String,
    role: String,
    email: String,
    isUserMe: Boolean,
    onMenuClicked: () -> Unit,  //TODO: replace
) {
    var showDropdownMenu by remember {
        mutableStateOf(false)
    }

    ListItem(
        leadingContent = {
            AnimatedVisibility(
                visible = profilePicUri == null,
                content = {
                    Icon(imageVector = Icons.Default.AccountCircle, contentDescription = null)
                }
            )

            AnimatedVisibility(
                visible = profilePicUri != null,
                content = {
                    AsyncImage(
                        model = profilePicUri,
                        contentDescription = null,
                        contentScale = ContentScale.Crop,
                        modifier = Modifier
                            .size(24.dp)
                            .clip(CircleShape)
                    )
                }
            )
        },
        headlineContent = {
            Text(text = name)
        },
        supportingContent = {
            Text(text = "$email ($role)")
        },
        trailingContent = {
            IconButton(
                onClick = { onMenuClicked() },
                content = {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null
                    )
                }
            )
        }
    )

    AnimatedVisibility(
        visible = showDropdownMenu,
        content = {
            DropdownMenu(
                expanded = showDropdownMenu,
                onDismissRequest = { showDropdownMenu = false },
                content = {
                    DropdownMenuItem(
                        text = { Text(text = "Message") },
                        onClick = {
                            showDropdownMenu = false
                            /*TODO*/
                        },
                        enabled = !isUserMe
                    )
                }
            )
        }
    )
}