package com.enoch02.helpdesk.data.remote.repository.firestore_db

import android.util.Log
import com.enoch02.helpdesk.data.remote.model.Ticket
import com.enoch02.helpdesk.data.remote.model.Tickets
import com.enoch02.helpdesk.data.remote.model.UserData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val USER_COLLECTION_NAME = "users/"
private const val TICKETS_COLLECTION_NAME = "tickets/"
private const val TAG = "FirestoreRepo"

class FirestoreRepositoryImpl @Inject constructor(private val db: FirebaseFirestore) :
    FirestoreRepository {
    override suspend fun createNewUserData(uid: String, name: String, role: String): Result<Unit> {
        return try {
            val userData = UserData(displayName = name, role = role)
            val collection = db.collection(USER_COLLECTION_NAME)

            collection.document(uid).set(userData).await()

            Result.success(Unit)
        } catch (e: Exception) {
            return Result.failure(e)
        }
    }

    override suspend fun getUserData(uid: String): Result<UserData?> {
        return try {
            val document = db.collection(USER_COLLECTION_NAME).document(uid)
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
            val document = db.collection(USER_COLLECTION_NAME).document(uid)
            document.update("displayName", newDisplayName).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "updateDisplayName: ${e.message}")
            Result.failure(e)
        }
    }

    //TODO: how can i add another without overwriting the list in the db??
    override suspend fun createTicket(ticket: Ticket): Result<Unit> {
        return try {
            val collection = db.collection(TICKETS_COLLECTION_NAME)
            val documentRef = collection.document(ticket.uid.toString())
            val tickets = documentRef.get().await()

            if (tickets.exists()) {
                val currentList = tickets.toObject(Tickets::class.java)
                currentList?.tickets?.add(ticket)

                if (currentList != null) {
                    documentRef.set(currentList)
                }
            } else {
                documentRef
                    .set(Tickets(tickets = mutableListOf(ticket)))
                    .await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "createTicket: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getTickets(uid: String): Result<Tickets> {
        return try {
            val documentRef = db.collection(TICKETS_COLLECTION_NAME).document(uid)
            val tickets = documentRef.get().await().toObject(Tickets::class.java)

            if (tickets != null) {
                Result.success(tickets)
            } else {
                // the user has not created any ticket, return an empty object
                Result.success(Tickets(tickets = mutableListOf()))
            }
        } catch (e: Exception) {
            Log.e(TAG, "createTicket: ${e.message}")
            Result.failure(e)
        }
    }
}