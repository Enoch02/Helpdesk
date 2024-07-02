package com.enoch02.helpdesk.ui.screen.common.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.Card
import androidx.compose.material3.ListItem
import androidx.compose.material3.RadioButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import com.enoch02.helpdesk.util.SORTING_CRITERIA

@Composable
fun SortingDialog(
    showSortingDialog: Boolean,
    onDismiss: () -> Unit,
    onConfirm: () -> Unit,
    currentSorting: String,
    onSelectionChange: (String) -> Unit,
) {
    if (showSortingDialog) {
        AlertDialog(
            onDismissRequest = { onDismiss() },
            confirmButton = {
                TextButton(
                    onClick = { onConfirm() },
                    content = {
                        Text(text = "OK")
                    }
                )
            },
            dismissButton = {
                TextButton(
                    onClick = { onDismiss() },
                    content = {
                        Text(text = "Cancel")
                    }
                )
            },
            title = {
                Text(text = "Sort by..")
            },
            text = {
                Card {
                    LazyColumn(
                        content = {
                            items(
                                count = SORTING_CRITERIA.size,
                                itemContent = { index ->
                                    ListItem(
                                        headlineContent = { Text(text = SORTING_CRITERIA[index]) },
                                        trailingContent = {
                                            RadioButton(
                                                selected = SORTING_CRITERIA[index] == currentSorting,
                                                onClick = {
                                                    onSelectionChange(SORTING_CRITERIA[index])
                                                }
                                            )
                                        },
                                        modifier = Modifier.clickable {
                                            onSelectionChange(SORTING_CRITERIA[index])
                                        }
                                    )
                                }
                            )
                        }
                    )
                }
            }
        )
    }
}