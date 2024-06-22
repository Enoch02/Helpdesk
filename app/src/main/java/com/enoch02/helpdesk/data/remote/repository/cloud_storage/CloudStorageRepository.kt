package com.enoch02.helpdesk.data.remote.repository.cloud_storage

import android.net.Uri

interface CloudStorageRepository {
    suspend fun uploadProfilePicture(uri: Uri, ownerID: String): Result<Unit>

    suspend fun getProfilePicture(ownerID: String): Result<Uri>

    suspend fun uploadChatImage(uri: Uri, chatId: String, imageId: String): Result<Unit>

    suspend fun getChatImage(chatID: String, imageId: String): Result<Uri>
}