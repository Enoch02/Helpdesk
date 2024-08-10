package com.enoch02.helpdesk.data.remote.repository.firestore_db

import android.util.Log
import com.enoch02.helpdesk.data.local.model.TicketStats
import com.enoch02.helpdesk.data.remote.model.Article
import com.enoch02.helpdesk.data.remote.model.Chat
import com.enoch02.helpdesk.data.remote.model.Feedback
import com.enoch02.helpdesk.data.remote.model.Message
import com.enoch02.helpdesk.data.remote.model.Ticket
import com.enoch02.helpdesk.data.remote.model.Tickets
import com.enoch02.helpdesk.data.remote.model.UserData
import com.enoch02.helpdesk.data.remote.model.contains
import com.enoch02.helpdesk.data.remote.repository.auth.FirebaseAuthRepository
import com.enoch02.helpdesk.util.DEFAULT_DISPLAY_NAME
import com.enoch02.helpdesk.util.getCurrentDateTime
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

private const val USER_COLLECTION_NAME = "users/"
private const val TICKETS_COLLECTION_NAME = "tickets/"
private const val ARTICLES_COLLECTION_NAME = "articles/"
const val CHATS_COLLECTION_NAME = "chats/"
const val FEEDBACK_COLLECTION_NAME = "feedbacks/"
private const val TAG = "FirestoreRepo"

class FirestoreRepositoryImpl @Inject constructor(
    private val db: FirebaseFirestore,
    private val authRepository: FirebaseAuthRepository,
) :
    FirestoreRepository {
    override suspend fun createNewUserData(uid: String, userData: UserData): Result<Unit> {
        return try {
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

    override suspend fun updateEmail(uid: String, newEmail: String): Result<Unit> {
        return try {
            val document = db.collection(USER_COLLECTION_NAME).document(uid)
            document.update("email", newEmail).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "updateEmail: ${e.message}")
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

    /**
     * Get all [Ticket] associated with a particular [uid]
     * @param uid user id
     * @return [Tickets]
     */
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

    /**
     * Get every [Ticket] stored in firestore (for staff)
     * @return [Tickets]
     */
    override suspend fun getTickets(): Result<List<Ticket>> {
        return try {
            val temp = mutableListOf<Ticket>()
            val tickets = db.collection(TICKETS_COLLECTION_NAME)
                .get()
                .await()

            tickets.forEach { ticket ->
                val obj = ticket.toObject(Tickets::class.java)
                temp.addAll(obj.tickets?.toList() ?: emptyList())
            }

            Result.success(temp)
        } catch (e: Exception) {
            Log.e(TAG, "getTickets: ${e.message}")
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

    override suspend fun closeTicket(uid: String, tid: String): Result<Unit> {
        return try {
            val documentRef = db.collection(TICKETS_COLLECTION_NAME).document(uid)
            val ticketsObj = documentRef.get().await().toObject(Tickets::class.java)
            val tickets = ticketsObj?.tickets
            val ticketIndex = tickets?.indexOfFirst { it.ticketID == tid }

            if (ticketsObj != null) {
                if (ticketIndex != null) {
                    val ticket = tickets[ticketIndex]

                    tickets[ticketIndex] = ticket.copy(
                        status = "Closed",
                        closedAt = getCurrentDateTime()
                    )
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
     * [uid] - user id of the ticket's owner
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

    override suspend fun startNewChat(chat: Chat): Result<String> {
        return try {
            val collection = db.collection(CHATS_COLLECTION_NAME)
            val documentRef = collection.document(chat.chatID.toString())

            documentRef.set(chat)

            Result.success(chat.chatID.toString())
        } catch (e: Exception) {
            Log.e(TAG, "startNewChat: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Get the [Chat] associated with a ticket object
     * */
    override suspend fun getChat(cid: String): Result<Chat> {
        return try {
            val documentRef = db.collection(CHATS_COLLECTION_NAME).document(cid)
            val chat = documentRef.get().await().toObject(Chat::class.java)

            Result.success(chat ?: Chat())
        } catch (e: Exception) {
            Log.e(TAG, "getChat: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun sendMessage(cid: String, newMessage: Message): Result<Unit> {
        return try {
            val documentRef = db.collection(CHATS_COLLECTION_NAME).document(cid)
            val chatObj = documentRef.get().await().toObject(Chat::class.java)

            if (chatObj != null) {
                val messages = chatObj.messages?.toMutableList()

                messages?.add(newMessage)

                documentRef
                    .set(chatObj.copy(messages = messages))
                    .await()
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "sendMessage: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getTicketStats(): Result<TicketStats> {
        return try {
            var total = 0
            var open = 0
            var closed = 0
            var unassigned = 0
            var assignedToMe = 0
            val tickets = db.collection(TICKETS_COLLECTION_NAME)
                .get()
                .await()
            val users = db.collection(USER_COLLECTION_NAME)
                .get()
                .await()

            tickets.forEach { ticket ->
                val obj = ticket.toObject(Tickets::class.java)

                total += obj.tickets?.size ?: 0
                open += obj.tickets?.filter { it.status == "Open" }?.size ?: 0
                closed += obj.tickets?.filter { it.status == "Closed" }?.size ?: 0
                unassigned += obj.tickets?.filter { it.staffID.isNullOrEmpty() }?.size ?: 0
                assignedToMe += obj.tickets?.filter { it.staffID == authRepository.getUID() }?.size
                    ?: 0
            }

            Result.success(
                TicketStats(
                    total = total,
                    open = open,
                    closed = closed,
                    unassigned = unassigned,
                    assignedToMe = assignedToMe,
                    users = users.size()
                )
            )
        } catch (e: Exception) {
            Log.e(TAG, "getStats: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getUsers(): Result<List<UserData>> {
        return try {
            val temp = mutableListOf<UserData>()
            val users = db.collection(USER_COLLECTION_NAME)
                .get()
                .await()

            users.forEach { user ->
                val obj = user.toObject(UserData::class.java)
                temp.add(obj)
            }

            Result.success(temp)
        } catch (e: Exception) {
            Log.e(TAG, "getUsers: ${e.message}")
            Result.failure(e)
        }
    }

    override suspend fun getUserName(uid: String): Result<String> {
        return try {
            val user = db.collection(USER_COLLECTION_NAME).document(uid)
                .get()
                .await()
                .toObject(UserData::class.java)

            Result.success(user?.displayName ?: DEFAULT_DISPLAY_NAME)
        } catch (e: Exception) {
            Log.e(TAG, "getUserName: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Get the list of [Chat] associated with a [uid]
     * */
    override suspend fun getChats(uid: String): Result<List<Chat>> {
        return try {
            val chats =
                db.collection(CHATS_COLLECTION_NAME).get().await().toObjects(Chat::class.java)

            Result.success(chats.filter { it.members?.contains(uid) ?: false })
        } catch (e: Exception) {
            Log.e(TAG, "getChats: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Send a [Feedback] that is not associated with any user to the database
     */
    override suspend fun sendFeedback(feedback: Feedback): Result<Unit> {
        return try {
            db.collection(FEEDBACK_COLLECTION_NAME)
                .add(feedback)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "sendFeedback: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Get a list of [Feedback]
     * */
    override suspend fun getFeedbacks(): Result<List<Feedback>> {
        return try {
            val feedbacks =
                db.collection(FEEDBACK_COLLECTION_NAME).get().await()
                    .toObjects(Feedback::class.java)

            Result.success(feedbacks)
        } catch (e: Exception) {
            Log.e(TAG, "getChats: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Create an [Article]
     */
    override suspend fun createArticle(article: Article): Result<Unit> {
        return try {
            db.collection(ARTICLES_COLLECTION_NAME)
                .add(article)
                .await()

            Result.success(Unit)
        } catch (e: Exception) {
            Log.e(TAG, "createArticle: ${e.message}")
            Result.failure(e)
        }
    }

    /**
     * Get all articles from the database
     */
    override suspend fun getArticles(): Result<List<Article>> {
        return try {
            val articles =
                db.collection(ARTICLES_COLLECTION_NAME).get().await().toObjects(Article::class.java)

            Result.success(articles)
        } catch (e: Exception) {
            Log.e(TAG, "getChats: ${e.message}")
            Result.failure(e)
        }
    }
}