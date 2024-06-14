package com.enoch02.helpdesk.ui.screen.common.settings.component

import android.net.Uri
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Logout
import androidx.compose.material.icons.filled.PersonOutline
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun UserInfo(
    name: String,
    email: String,
    profilePic: Uri?,
    onProfilePicChange: () -> Unit,
    onLogOutClicked: () -> Unit,
    modifier: Modifier
) {
    ListItem(
        leadingContent = {
            if (profilePic == null) {
                Icon(imageVector = Icons.Default.PersonOutline, contentDescription = null)
            } else {
                AsyncImage(
                    model = profilePic,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp)
                )
            }
        },
        headlineContent = {
            Text(text = name)
        },
        supportingContent = {
            Text(text = email)
        },
        trailingContent = {
            IconButton(
                onClick = { onLogOutClicked() },
                content = {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Logout,
                        contentDescription = "log out"
                    )
                }
            )
        },
        tonalElevation = 48.dp,
        modifier = modifier
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    UserInfo(
        name = "Enoch",
        email = "adesanyaenoch@gmail.com",
        profilePic = null,
        onProfilePicChange = {},
        onLogOutClicked = { /*TODO*/ },
        modifier = Modifier.fillMaxWidth()
    )
}
