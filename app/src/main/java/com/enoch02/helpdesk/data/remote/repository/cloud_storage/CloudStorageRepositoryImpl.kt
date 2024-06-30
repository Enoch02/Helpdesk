package com.enoch02.helpdesk.data.remote.repository.cloud_storage

import android.net.Uri
import android.util.Log
import com.enoch02.helpdesk.data.remote.model.Ticket
import com.google.firebase.storage.FirebaseStorage
import kotlinx.coroutines.tasks.await
import java.util.UUID
import javax.inject.Inject

private const val PROFILE_PICS_COLLECTION_NAME = "profile_images/"
private const val CHAT_IMAGE_COLLECTION_NAME = "chat_images/"
private const val TICKET_ATTACHMENT_COLLECTION_NAME = "ticket_attachments/"
private const val TAG = "CloudStorageRepository"

class CloudStorageRepositoryImpl @Inject constructor(private val firebaseStorage: FirebaseStorage) :
    CloudStorageRepository {
    private val storageRef = firebaseStorage.reference

    override suspend fun uploadProfilePicture(uri: Uri, ownerID: String): Result<Unit> {
        return try {
            val imageRef = storageRef.child("$PROFILE_PICS_COLLECTION_NAME$ownerID")
            imageRef.putFile(uri).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getProfilePicture(ownerID: String): Result<Uri> {
        return try {
            val imageRef = storageRef.child("$PROFILE_PICS_COLLECTION_NAME$ownerID")
            Result.success(imageRef.downloadUrl.await())
        } catch (e: Exception) {
            Log.d(TAG, "getProfilePicture: $e")
            Result.failure(e)
        }
    }

    override suspend fun uploadChatImage(uri: Uri, chatId: String, imageId: String): Result<Unit> {
        return try {
            val imageRef = storageRef.child("$CHAT_IMAGE_COLLECTION_NAME$chatId/$imageId")
            imageRef.putFile(uri).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun getChatImage(chatID: String, imageId: String): Result<Uri> {
        return try {
            val imageRef = storageRef.child("$CHAT_IMAGE_COLLECTION_NAME$chatID/$imageId")

            Result.success(imageRef.downloadUrl.await())
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * [attachments] - files to upload
     * [ticketId] - id of the [Ticket] that owns these attachments
     *
     * Upload attachments related to a specific ticket
     * */
    override suspend fun uploadTicketAttachments(
        attachments: List<Uri>,
        ticketId: String,
    ): Result<Unit> {
        return try {
            attachments.forEach { attachment ->
                val attachmentRef = storageRef.child(
                    "$TICKET_ATTACHMENT_COLLECTION_NAME$ticketId/${UUID.randomUUID()}"
                )

                attachmentRef.putFile(attachment).await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    /**
     * Find the attachments attached to a [ticketId]
     * */
    override suspend fun getTicketAttachments(ticketId: String): Result<List<Uri>> {
        return try {
            val temp = mutableListOf<Uri>()
            val attachments = storageRef.child("$TICKET_ATTACHMENT_COLLECTION_NAME/$ticketId")
                .listAll()
                .await()

            attachments.items.forEach {
                temp.add(it.downloadUrl.await())
            }

            Result.success(temp)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}