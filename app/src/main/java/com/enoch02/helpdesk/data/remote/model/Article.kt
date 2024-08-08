package com.enoch02.helpdesk.data.remote.model

import com.enoch02.helpdesk.util.getCurrentDateTime
import java.util.Date
import java.util.UUID

data class Article(
    val articleId: String? = UUID.randomUUID().toString(),
    val authorId: String? = null,
    val title: String? = null,
    val content: String? = null,
    val creationDate: Date? = getCurrentDateTime()
)
