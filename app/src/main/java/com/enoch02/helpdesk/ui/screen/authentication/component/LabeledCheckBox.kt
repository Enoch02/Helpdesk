package com.enoch02.helpdesk.ui.screen.authentication.component

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.width
import androidx.compose.material3.Checkbox
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp

@Composable
fun LabeledCheckBox(
    label: String,
    checked: Boolean,
    onCheckChanged: (Boolean) -> Unit,
    modifier: Modifier
) {
    Row(
        modifier = modifier.clickable { onCheckChanged(checked.not()) },
        verticalAlignment = Alignment.CenterVertically,
        content = {
            Checkbox(checked = checked, onCheckedChange = { onCheckChanged(it) })
            Spacer(modifier = Modifier.width(4.dp))
            Text(text = label)
            Spacer(modifier = Modifier.width(8.dp))
        }
    )
}

@Preview(showSystemUi = true, showBackground = true)
@Composable
private fun Preview() {
    LabeledCheckBox(
        label = "Hello World",
        checked = true,
        onCheckChanged = {},
        modifier = Modifier.fillMaxWidth()
    )
}