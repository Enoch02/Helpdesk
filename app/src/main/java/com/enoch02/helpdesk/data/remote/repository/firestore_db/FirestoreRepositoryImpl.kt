package com.enoch02.helpdesk.data.remote.repository.firestore_db

import android.util.Log
import com.enoch02.helpdesk.data.remote.model.UserData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val COLLECTION_NAME = "users/"
private const val TAG = "FirestoreRepo"

class FirestoreRepositoryImpl @Inject constructor(private val db: FirebaseFirestore) :
    FirestoreRepository {
    override suspend fun createNewUserData(uid: String, name: String, role: String): Result<Unit> {
        return try {
            val userData = UserData(displayName = name, role = role)
            val collection = db.collection(COLLECTION_NAME)

            collection.document(uid).set(userData).await()

            Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun getUserData(uid: String): Result<UserData?> {
        return try {
            val document = db.collection(COLLECTION_NAME).document(uid)
                .get()
                .await()
                .toObject(UserData::class.java)

            Result.success(document)
        } catch (e: Exception) {
            Log.e(TAG, "getUserData: ${e.message}")
            return Result.failure(e)
        }
    }

    override suspend fun updateDisplayName(uid: String, newDisplayName: String): Result<Unit> {
        return try {
            val document = db.collection(COLLECTION_NAME).document(uid)
            document.update("displayName", newDisplayName).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "updateDisplayName: ${e.message}")
            return Result.failure(e)
        }
    }
}