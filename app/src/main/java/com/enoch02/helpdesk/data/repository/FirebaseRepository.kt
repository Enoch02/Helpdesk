package com.enoch02.helpdesk.data.repository

import com.enoch02.helpdesk.util.Resource
import com.google.firebase.auth.AuthResult
import kotlinx.coroutines.flow.Flow

interface FirebaseRepository {
    fun isUserLoggedIn(): Boolean
    fun loginUser(email: String, password: String): Flow<Resource<AuthResult>>
    fun registerUser(email: String, password: String): Flow<Resource<AuthResult>>
}


