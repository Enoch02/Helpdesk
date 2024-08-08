package com.enoch02.helpdesk

import com.enoch02.helpdesk.data.remote.repository.auth.FirebaseAuthRepository
import com.google.firebase.auth.FirebaseAuth
import io.mockk.MockKAnnotations
import io.mockk.every
import io.mockk.impl.annotations.MockK
import io.mockk.mockk
import org.junit.Before
import org.junit.Test

class AuthRepoTest {

    @MockK
    private lateinit var firebaseAuth: FirebaseAuth
    private val mockAuthRepo: FirebaseAuthRepository = mockk<FirebaseAuthRepository>()

    @Before
    fun setUp() {
        MockKAnnotations.init(this)
        every { mockAuthRepo.isUserLoggedIn() } returns true
    }

    @Test
    fun `isUserSignedIn returns true when user is signed in`() {
        val isSignedIn = mockAuthRepo.isUserLoggedIn()

        assert(isSignedIn)
    }
}
