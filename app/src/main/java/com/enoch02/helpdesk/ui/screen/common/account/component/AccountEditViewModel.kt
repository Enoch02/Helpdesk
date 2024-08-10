package com.enoch02.helpdesk.ui.screen.common.account.component

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.ViewModel
import com.enoch02.helpdesk.data.remote.repository.auth.FirebaseAuthRepository
import com.enoch02.helpdesk.util.restartActivity
import com.google.firebase.auth.EmailAuthProvider
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountEditViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
) :
    ViewModel() {

    fun changeEmail(context: Context, password: String, newEmail: String) {
        val credential = EmailAuthProvider.getCredential(authRepository.getMail(), password)
        authRepository.getUser()?.reauthenticate(credential)
            ?.addOnSuccessListener {
                authRepository.getUser()!!.verifyBeforeUpdateEmail(newEmail)
                    .addOnCompleteListener {
                        Toast.makeText(
                            context,
                            "Check your mail to complete the change",
                            Toast.LENGTH_SHORT
                        )
                            .show()
                        authRepository.signOut()
                        restartActivity(context)
                    }
            }
            ?.addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
    }

    fun changePassword(context: Context, currentPassword: String, newPassword: String) {
        val credential = EmailAuthProvider.getCredential(authRepository.getMail(), currentPassword)
        authRepository.getUser()?.reauthenticate(credential)
            ?.addOnSuccessListener {
                authRepository.getUser()!!.updatePassword(newPassword)
                    .addOnCompleteListener {
                        Toast.makeText(context, "Password changed successfully", Toast.LENGTH_SHORT)
                            .show()
                        authRepository.signOut()
                        restartActivity(context)
                    }
            }
            ?.addOnFailureListener {
                Toast.makeText(context, it.message, Toast.LENGTH_SHORT).show()
            }
    }
}