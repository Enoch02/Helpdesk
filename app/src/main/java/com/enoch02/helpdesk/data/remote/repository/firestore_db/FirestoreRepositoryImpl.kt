package com.enoch02.helpdesk.data.remote.repository.firestore_db

import android.util.Log
import com.enoch02.helpdesk.data.remote.model.Chat
import com.enoch02.helpdesk.data.remote.model.Chats
import com.enoch02.helpdesk.data.remote.model.Ticket
import com.enoch02.helpdesk.data.remote.model.Tickets
import com.enoch02.helpdesk.data.remote.model.UserData
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val USER_COLLECTION_NAME = "users/"
private const val TICKETS_COLLECTION_NAME = "tickets/"
private const val CHATS_COLLECTION_NAME = "chats/"
private const val MESSAGES_COLLECTION_NAME = "messages/"
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

    override suspend fun getTicket(uid: String, tid: String): Result<Ticket> {
        return try {
            val documentRef = db.collection(TICKETS_COLLECTION_NAME).document(uid)
            val tickets = documentRef.get().await().toObject(Tickets::class.java)?.tickets

            Result.success(tickets?.firstOrNull { it.ticketID == tid } ?: Ticket())
        } catch (e: Exception) {
            Log.e(TAG, "getTicket: ${e.message}")
            Result.failure(e)
        }
    }

    //TODO: get around to combining these 2 functions
    override suspend fun closeTicket(uid: String, tid: String): Result<Unit> {
        return try {
            val documentRef = db.collection(TICKETS_COLLECTION_NAME).document(uid)
            val ticketsObj = documentRef.get().await().toObject(Tickets::class.java)
            val tickets = ticketsObj?.tickets
            val ticketIndex = tickets?.indexOfFirst { it.ticketID == tid }

            if (ticketsObj != null) {
                if (ticketIndex != null) {
                    val ticket = tickets[ticketIndex]

                    tickets[ticketIndex] = ticket.copy(status = "Closed")
                }

                documentRef
                    .set(ticketsObj)
                    .await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "closeTicket: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun openTicket(uid: String, tid: String): Result<Unit> {
        return try {
            val documentRef = db.collection(TICKETS_COLLECTION_NAME).document(uid)
            val ticketsObj = documentRef.get().await().toObject(Tickets::class.java)
            val tickets = ticketsObj?.tickets
            val ticketIndex = tickets?.indexOfFirst { it.ticketID == tid }

            if (ticketsObj != null) {
                if (ticketIndex != null) {
                    val ticket = tickets[ticketIndex]

                    tickets[ticketIndex] = ticket.copy(status = "Open")
                }

                documentRef
                    .set(ticketsObj)
                    .await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "closeTicket: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * [uid] - user's id
     * [tid] - ticket id
     * [newTicket] - modified ticket object
     *
     * Replace the ticket that matches the given [tid] with [newTicket]
     * */
    override suspend fun updateTicket(uid: String, tid: String, newTicket: Ticket): Result<Unit> {
        return try {
            val documentRef = db.collection(TICKETS_COLLECTION_NAME).document(uid)
            val ticketsObj = documentRef.get().await().toObject(Tickets::class.java)
            val tickets = ticketsObj?.tickets
            val ticketIndex = tickets?.indexOfFirst { it.ticketID == tid }

            if (ticketsObj != null) {
                if (ticketIndex != null) {
                    tickets[ticketIndex] = newTicket
                }

                documentRef
                    .set(ticketsObj)
                    .await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "updateTicket: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun startNewChat(chat: Chat, tid: String): Result<String> {
        return try {
            val collection = db.collection(CHATS_COLLECTION_NAME)
            val documentRef = collection.document(tid)  // chats documents use tid as name
            val chats = documentRef.get().await()

            if (chats.exists()) {
                val currentChats = chats.toObject(Chats::class.java)
                currentChats?.chats?.add(chat)

                if (currentChats != null)
                    documentRef.set(currentChats)
            } else {
                documentRef
                    .set(Chats(chats = mutableListOf(chat)))
            }

            Result.success(chat.chatID.toString())
        } catch (e: Exception) {
            Log.e(TAG, "startNewChat: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getChat(tid: String, cid: String): Result<Chat> {
        return try {
            val documentRef = db.collection(CHATS_COLLECTION_NAME).document(tid)
            val chats = documentRef.get().await().toObject(Chats::class.java)?.chats

            Result.success(chats?.firstOrNull { it.chatID == cid } ?: Chat())
        } catch (e: Exception) {
            Log.e(TAG, "getChat: ${e.message}")
            Result.failure(e)
        }
    }
}