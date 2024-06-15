package com.enoch02.helpdesk.ui.screen.student.create_ticket.component

import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.text.KeyboardActions
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.KeyboardArrowDown
import androidx.compose.material3.DropdownMenu
import androidx.compose.material3.DropdownMenuItem
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.saveable.rememberSaveable
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.focus.FocusDirection
import androidx.compose.ui.platform.LocalFocusManager
import androidx.compose.ui.text.input.ImeAction
import androidx.compose.ui.unit.dp
import com.enoch02.helpdesk.data.local.model.Category

@Composable
fun FormDropdown(
    selection: String,
    label: String,
    options: List<String>,
    allowCustomOptions: Boolean,
    onSelectionChange: (String) -> Unit
) {
    val focusManager = LocalFocusManager.current
    var showDropDown by rememberSaveable {
        mutableStateOf(false)
    }

    OutlinedTextField(
        value = selection,
        onValueChange = { onSelectionChange(it) },
        label = { Text(text = label) },
        keyboardOptions = KeyboardOptions(
            autoCorrect = true,
            imeAction = ImeAction.Done
        ),
        keyboardActions = KeyboardActions(
            onDone = {
                if (!Category.builtInCategories.contains(selection)) {
                    Category.builtInCategories.add(selection)
                }
                focusManager.moveFocus(FocusDirection.Down)
            }
        ),
        trailingIcon = {
            IconButton(
                onClick = { showDropDown = true },
                content = {
                    Icon(
                        imageVector = Icons.Default.KeyboardArrowDown,
                        contentDescription = "Expand dropdown"
                    )
                }
            )

            DropdownMenu(
                expanded = showDropDown,
                onDismissRequest = { showDropDown = false },
                content = {
                    options.forEach { entry ->
                        DropdownMenuItem(
                            text = { Text(text = entry) },
                            onClick = {
                                onSelectionChange(entry)
                                showDropDown = false
                                focusManager.moveFocus(FocusDirection.Down)
                            }
                        )
                    }
                },
                modifier = Modifier.fillMaxWidth(0.5f)
            )
        },
        modifier = Modifier
            .fillMaxWidth()
            .padding(bottom = 8.dp),
        readOnly = allowCustomOptions.not()
    )
}