package com.enoch02.helpdesk.data.local.model

import android.net.Uri

data class ChatsData(
    val profilePic: Uri?,
    val name: String?,
    val ticketSubject: String?,
    val mostRecentMessage: String?,
)
