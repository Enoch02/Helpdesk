package com.enoch02.helpdesk.ui.screen.staff.feedback_viewer.component

import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RatingBar(rating: Int, modifier: Modifier) {
    Row(
        modifier = modifier,
        content = {
            repeat(5) { index ->
                val filled = index < rating
                Icon(
                    imageVector = if (filled) Icons.Filled.Star else Icons.Filled.StarBorder,
                    contentDescription = null,
                    modifier = Modifier.size(24.dp),
                    tint = if (filled) Color.Yellow else Color.Gray
                )
            }
        }
    )
}
