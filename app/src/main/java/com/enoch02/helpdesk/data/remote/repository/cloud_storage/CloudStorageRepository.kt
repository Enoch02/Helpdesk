package com.enoch02.helpdesk.data.remote.repository.cloud_storage

import android.net.Uri

interface CloudStorageRepository {
    suspend fun uploadProfilePicture(uri: Uri, ownerID: String): Result<Unit>

    suspend fun getProfilePicture(ownerID: String): Result<Uri>
}