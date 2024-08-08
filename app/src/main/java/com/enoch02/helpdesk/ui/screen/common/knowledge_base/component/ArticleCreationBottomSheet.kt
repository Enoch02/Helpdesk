package com.enoch02.helpdesk.ui.screen.common.knowledge_base.component

import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material3.Button
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.ModalBottomSheet
import androidx.compose.material3.Surface
import androidx.compose.material3.Text
import androidx.compose.material3.TextField
import androidx.compose.material3.rememberModalBottomSheetState
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun ArticleCreationBottomSheet(
    showBottomSheet: Boolean,
    onDismiss: () -> Unit,
    onSubmit: (title: String, content: String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    val sheetState = rememberModalBottomSheetState(skipPartiallyExpanded = true)
    var title by rememberSaveable {
        mutableStateOf("")
    }
    var content by rememberSaveable {
        mutableStateOf("")
    }

    Surface {
        if (showBottomSheet) {
            ModalBottomSheet(
                onDismissRequest = { onDismiss() },
                sheetState = sheetState,
                content = {
                    TextField(
                        value = title,
                        onValueChange = { title = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.2f),
                        label = { Text(text = "Title") },
                        keyboardOptions = KeyboardOptions(
                            capitalization = KeyboardCapitalization.Sentences,
                            keyboardType = KeyboardType.Text,
                            imeAction = ImeAction.Next,
                            autoCorrect = true
                        ),
                        keyboardActions = KeyboardActions(
                            onNext = {
                                focusManager.moveFocus(
                                    FocusDirection.Down
                                )
                            }
                        )
                    )

                    TextField(
                        value = content,
                        onValueChange = { content = it },
                        modifier = Modifier
                            .fillMaxWidth()
                            .weight(0.7f),
                        label = { Text(text = "Content") }
                    )

                    Box(
                        contentAlignment = Alignment.CenterEnd,
                        content = {
                            Button(
                                onClick = {
                                    onSubmit(title, content)
                                    title = ""
                                    content = ""
                                },
                                content = {
                                    Text("Create Article")
                                }
                            )
                        },
                        modifier = Modifier
                            .weight(0.1f)
                            .fillMaxWidth()
                    )
                }
            )
        }
    }
}