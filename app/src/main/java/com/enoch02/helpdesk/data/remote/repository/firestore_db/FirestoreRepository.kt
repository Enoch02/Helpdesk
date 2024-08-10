package com.enoch02.helpdesk.data.remote.repository.firestore_db

import com.enoch02.helpdesk.data.local.model.TicketStats
import com.enoch02.helpdesk.data.remote.model.Article
import com.enoch02.helpdesk.data.remote.model.Chat
import com.enoch02.helpdesk.data.remote.model.Feedback
import com.enoch02.helpdesk.data.remote.model.Message
import com.enoch02.helpdesk.data.remote.model.Ticket
import com.enoch02.helpdesk.data.remote.model.Tickets
import com.enoch02.helpdesk.data.remote.model.UserData

interface FirestoreRepository {
    suspend fun createNewUserData(uid: String, userData: UserData): Result<Unit>

    suspend fun getUserData(uid: String): Result<UserData?>

    suspend fun updateDisplayName(uid: String, newDisplayName: String): Result<Unit>

    suspend fun updateEmail(uid: String, newEmail: String): Result<Unit>

    suspend fun createTicket(ticket: Ticket): Result<Unit>

    suspend fun getTickets(uid: String): Result<Tickets>

    suspend fun getTickets(): Result<List<Ticket>>

    suspend fun getTicket(uid: String, tid: String): Result<Ticket>

    suspend fun closeTicket(uid: String, tid: String): Result<Unit>

    suspend fun openTicket(uid: String, tid: String): Result<Unit>

    suspend fun updateTicket(uid: String, tid: String, newTicket: Ticket): Result<Unit>

    suspend fun startNewChat(chat: Chat): Result<String>

    suspend fun getChat(cid: String): Result<Chat>

    suspend fun sendMessage(cid: String, newMessage: Message): Result<Unit>

    suspend fun getTicketStats(): Result<TicketStats>

    suspend fun getUsers(): Result<List<UserData>>

    suspend fun getUserName(uid: String): Result<String>

    suspend fun getChats(uid: String): Result<List<Chat>>

    suspend fun sendFeedback(feedback: Feedback): Result<Unit>

    suspend fun getFeedbacks(): Result<List<Feedback>>

    suspend fun createArticle(article: Article): Result<Unit>

    suspend fun getArticles(): Result<List<Article>>
}