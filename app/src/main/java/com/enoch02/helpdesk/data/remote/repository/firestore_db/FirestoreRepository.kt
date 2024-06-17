package com.enoch02.helpdesk.data.remote.repository.firestore_db

import com.enoch02.helpdesk.data.local.model.TicketStats
import com.enoch02.helpdesk.data.remote.model.Chat
import com.enoch02.helpdesk.data.remote.model.Message
import com.enoch02.helpdesk.data.remote.model.Ticket
import com.enoch02.helpdesk.data.remote.model.Tickets
import com.enoch02.helpdesk.data.remote.model.UserData

interface FirestoreRepository {
    suspend fun createNewUserData(uid: String, name: String, role: String): Result<Unit>

    suspend fun getUserData(uid: String): Result<UserData?>

    suspend fun updateDisplayName(uid: String, newDisplayName: String): Result<Unit>

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
}