package com.enoch02.helpdesk.data.remote.repository.firestore_db

import com.enoch02.helpdesk.data.remote.model.Ticket
import com.enoch02.helpdesk.data.remote.model.Tickets
import com.enoch02.helpdesk.data.remote.model.UserData

interface FirestoreRepository {
    suspend fun createNewUserData(uid: String, name: String, role: String): Result<Unit>

    suspend fun getUserData(uid: String): Result<UserData?>

    suspend fun updateDisplayName(uid: String, newDisplayName: String): Result<Unit>

    suspend fun createTicket(ticket: Ticket): Result<Unit>

    suspend fun getTickets(uid: String): Result<Tickets>
}