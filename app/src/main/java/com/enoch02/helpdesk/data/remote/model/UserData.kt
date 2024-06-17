package com.enoch02.helpdesk.data.remote.model

import com.google.firebase.firestore.IgnoreExtraProperties

@IgnoreExtraProperties
data class UserData(
    val displayName: String? = null,
    val email: String? = null,
    val role: String? = null,
    val chats: List<String>? = null // chatIds
)
