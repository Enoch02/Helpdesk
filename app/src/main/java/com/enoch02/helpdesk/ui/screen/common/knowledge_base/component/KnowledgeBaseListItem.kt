package com.enoch02.helpdesk.ui.screen.common.knowledge_base.component

import android.os.Build
import android.widget.Toast
import androidx.compose.foundation.clickable
import androidx.compose.material.icons.Icons
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
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.tooling.preview.Preview
import java.time.Instant
import java.util.Date

@Composable
fun KnowledgeBaseListItem(
    title: String,
    author: String,
    creationDate: String,
    onClick: () -> Unit
) {
    var showDropdown by remember {
        mutableStateOf(false)
    }
    val context = LocalContext.current

    ListItem(
        headlineContent = { Text(text = title) },
        supportingContent = { Text(text = "by $author at $creationDate") },
        trailingContent = {
            IconButton(
                onClick = { showDropdown = true },
                content = {
                    Icon(
                        imageVector = Icons.Default.MoreVert,
                        contentDescription = null
                    )

                    DropdownMenu(
                        expanded = showDropdown,
                        onDismissRequest = { showDropdown = false },
                        content = {
                            DropdownMenuItem(
                                text = { Text(text = "Edit") },
                                onClick = {
                                    /*TODO*/
                                    Toast.makeText(context, "Coming soon!!!", Toast.LENGTH_SHORT).show()
                                    showDropdown = false
                                }
                            )

                            /*DropdownMenuItem(
                                text = { Text(text = "Delete") },
                                onClick = { *//*TODO*//* }
                            )*/
                        }
                    )
                }
            )
        },
        modifier = Modifier.clickable {
            onClick()
        }
    )
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        KnowledgeBaseListItem(
            title = "How to do this and that",
            author = "Enoch",
            creationDate = Date.from(Instant.now()).toString(),
            onClick = {}
        )
    }
}