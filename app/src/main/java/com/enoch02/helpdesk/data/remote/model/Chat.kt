package com.enoch02.helpdesk.data.remote.model

import java.util.Date
import java.util.UUID

data class Chat(
    val chatID: String? = UUID.randomUUID().toString(),
    val ticketID: String? = null,
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

/**
 * [imageId] should be set only when the message is being sent with an image
 * */
data class Message(
    val messageText: String? = null,
    val imageId: String? = null,
    val sentAt: Date? = null,
    val sentBy: String? = null,
    val type: MessageType? = null,
    val read: Boolean? = false
)

enum class MessageType {
    TEXT,
    IMAGE,
    IMAGE_AND_TEXT
}

/*data class Chats(
    val chats: MutableList<Chat>? = null
)*/
