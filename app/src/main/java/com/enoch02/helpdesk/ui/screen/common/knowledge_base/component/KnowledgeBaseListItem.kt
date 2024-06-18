package com.enoch02.helpdesk.ui.screen.common.knowledge_base.component

import android.os.Build
import androidx.compose.material3.ListItem
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.tooling.preview.Preview
import java.time.Instant
import java.util.Date

@Composable
fun KnowledgeBaseListItem(title: String, author: String, creationDate: String) {
    ListItem(
        headlineContent = { Text(text = title) },
        supportingContent = { Text(text = "by $author at $creationDate") }
    )
}

@Preview(showSystemUi = true)
@Composable
private fun Preview() {
    if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
        KnowledgeBaseListItem(
            title = "How to do this and that", author = "Enoch", creationDate = Date.from(
                Instant.now()
            ).toString()
        )
    }
}