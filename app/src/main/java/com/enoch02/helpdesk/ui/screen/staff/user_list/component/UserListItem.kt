package com.enoch02.helpdesk.ui.screen.staff.user_list.component

import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.MoreVert
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import coil.compose.AsyncImage

@Composable
fun UserListItem(
    profilePicUrl: String,
    name: String,
    role: String,
    email: String,
    onMenuClicked: () -> Unit,
) {
    ListItem(
        leadingContent = {
            AsyncImage(model = profilePicUrl, contentDescription = null)
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
}