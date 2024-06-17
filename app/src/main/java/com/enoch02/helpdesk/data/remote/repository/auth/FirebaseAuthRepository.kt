package com.enoch02.helpdesk.data.remote.repository.auth

import android.net.Uri
import com.enoch02.helpdesk.util.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.FirebaseUser
import kotlinx.coroutines.flow.Flow

interface FirebaseAuthRepository {
    fun isUserLoggedIn(): Boolean
    fun signOut()
    fun getUID(): String
    fun getMail(): String
    fun loginUser(email: String, password: String): Flow<Resource<AuthResult>>
    fun registerUser(email: String, password: String): Flow<Resource<AuthResult>>
    fun getUser(): FirebaseUser?
    fun updateUserInfo(name: String): Result<Unit>
    fun updatePassword(email: String, oldPassword: String, newPassword: String): Result<Unit>
}


