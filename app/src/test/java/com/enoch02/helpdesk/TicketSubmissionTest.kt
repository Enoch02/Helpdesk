package com.enoch02.helpdesk

import com.enoch02.helpdesk.data.remote.model.Ticket
import com.enoch02.helpdesk.data.remote.repository.auth.FirebaseAuthRepository
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import io.mockk.every
import io.mockk.mockk
import kotlinx.coroutines.runBlocking
import org.junit.Before
import org.junit.Test

class TicketSubmissionTest {
    private val mockAuthRepo: FirebaseAuthRepository = mockk<FirebaseAuthRepository>()
    private val mockFirestoreRepo: FirestoreRepository = mockk<FirestoreRepository>()
    private lateinit var ticket: Ticket


    @Before
    fun setUp() {
        every { mockAuthRepo.isUserLoggedIn() } returns true
        every { mockAuthRepo.getUID() } returns "1"

        ticket = Ticket(
            uid = mockAuthRepo.getUID(),
            subject = "Hello, World!",
            category = "Test",
            status = "Open",
            priority = "High"
        )

        every { runBlocking { mockFirestoreRepo.createTicket(ticket) } } returns Result.success(Unit)
    }

    @Test
    fun submitTicket() {
        runBlocking {
            mockFirestoreRepo.createTicket(ticket)
                .onSuccess {
                    assert(true)
                }
                .onFailure {
                    assert(false)
                }
        }
    }
}
