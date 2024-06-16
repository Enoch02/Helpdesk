package com.enoch02.helpdesk.data.remote.model

import java.util.Date
import java.util.UUID

data class Chat(
    val chatID: String? = UUID.randomUUID().toString(),
    val createdAt: Date? = null,
    val startedBy: String? = null,  // uid
    // TODO: the uid of the person assigned to this chat will be added here
    val members: Members? = null,  // uid (limit = 2),
    val messages: List<Message>? = null
)

data class Members(
    val userID: String? = null,
    val staffID: String? = null
)

data class Chats(
    val chats: MutableList<Chat>? = null
)
