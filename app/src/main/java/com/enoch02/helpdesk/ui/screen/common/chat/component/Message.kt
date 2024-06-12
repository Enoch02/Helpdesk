package com.enoch02.helpdesk.ui.screen.common.chat.component

//TODO: might remove, currently for demo purposes
data class Message(
    val text: String,
    val owner: BubbleOwner,
    val type: BubbleType,
    val url: String
)
