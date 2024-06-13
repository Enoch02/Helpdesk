package com.enoch02.helpdesk.ui.screen.student.feedback.component

import androidx.compose.animation.animateColorAsState
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.size
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Star
import androidx.compose.material.icons.filled.StarBorder
import androidx.compose.material3.Icon
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.unit.dp

@Composable
fun RatingPicker(
    rating: Int,
    onRatingChanged: (Int) -> Unit,
    modifier: Modifier = Modifier
) {
    Row(modifier = modifier) {
        for (i in 1..5) {
            val isFilled = i <= rating
            val color by animateColorAsState(
                targetValue = if (isFilled) Color.Yellow else Color.Gray,
                label = ""
            )
            val starIcon = if (isFilled) {
                Icons.Filled.Star
            } else {
                Icons.Filled.StarBorder
            }
            Icon(
                imageVector = starIcon,
                contentDescription = null,
                tint = color,
                modifier = Modifier
                    .clickable { onRatingChanged(i) }
                    .size(48.dp)
            )
        }
    }
}