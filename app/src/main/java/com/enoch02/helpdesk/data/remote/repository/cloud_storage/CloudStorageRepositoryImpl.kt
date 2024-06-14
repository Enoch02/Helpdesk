package com.enoch02.helpdesk.data.remote.repository.cloud_storage

import android.net.Uri
import android.util.Log
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val COLLECTION_NAME = "profile_images/"
private const val TAG = "CloudStorageRepository"

class CloudStorageRepositoryImpl @Inject constructor(private val firebaseStorage: FirebaseStorage) :
    CloudStorageRepository {
    private val storageRef = firebaseStorage.reference

    override suspend fun uploadProfilePicture(uri: Uri, ownerID: String): Result<Unit> {
        return try {
            val imageRef = storageRef.child("$COLLECTION_NAME$ownerID")
            imageRef.putFile(uri).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProfilePicture(ownerID: String): Result<Uri> {
        return try {
            val imageRef = storageRef.child("$COLLECTION_NAME$ownerID")
            Result.success(imageRef.downloadUrl.await())
        } catch (e: Exception) {
            Log.d(TAG, "getProfilePicture: $e")
            Result.failure(e)
        }
    }
}