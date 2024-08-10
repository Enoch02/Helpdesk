package com.enoch02.helpdesk.data.remote.repository.auth

import android.util.Log
import com.enoch02.helpdesk.data.local.model.Resource
import com.google.firebase.auth.AuthResult
import com.google.firebase.auth.EmailAuthProvider
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.userProfileChangeRequest
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.flow.flow
import kotlinx.coroutines.tasks.await
import javax.inject.Inject

class FirebaseAuthRepositoryImpl @Inject constructor(private val firebaseAuth: FirebaseAuth) :
    FirebaseAuthRepository {
    override fun isUserLoggedIn(): Boolean {
        return firebaseAuth.currentUser != null
    }

    override fun signOut() {
        firebaseAuth.signOut()
    }

    override fun getUID(): String = firebaseAuth.currentUser?.uid.toString()
    override fun getMail(): String = firebaseAuth.currentUser?.email.toString()

    override fun loginUser(email: String, password: String): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.signInWithEmailAndPassword(email, password).await()
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun registerUser(email: String, password: String): Flow<Resource<AuthResult>> {
        return flow {
            emit(Resource.Loading())
            val result = firebaseAuth.createUserWithEmailAndPassword(email, password).await()
            emit(Resource.Success(result))
        }.catch {
            emit(Resource.Error(it.message.toString()))
        }
    }

    override fun getUser() = firebaseAuth.currentUser

    override fun updateUserInfo(name: String): Result<Unit> {
        return try {
            val user = firebaseAuth.currentUser
            val profileUpdates = userProfileChangeRequest {
                if (name != (getUser()?.displayName ?: "")) {
                    displayName = name
                }
            }

            user!!.updateProfile(profileUpdates).addOnCompleteListener {
                val updatedUser = firebaseAuth.currentUser

                Log.d(
                    "TAG",
                    "updateUserInfo: ${updatedUser?.displayName.toString()}. Image: ${updatedUser?.photoUrl}"
                )
            }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override fun updatePassword(
        email: String,
        oldPassword: String,
        newPassword: String,
    ): Result<Unit> {
        return try {
            val user = firebaseAuth.currentUser
            val credential = EmailAuthProvider.getCredential(email, oldPassword)

            user!!.reauthenticate(credential)
                .addOnCompleteListener {
                    user.updatePassword(newPassword)
                }

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }

    override suspend fun resetPassword(email: String): Result<Unit> {
        return try {
            firebaseAuth.sendPasswordResetEmail(email).await()

            Result.success(Unit)
        } catch (e: Exception) {
            Result.failure(e)
        }
    }
}