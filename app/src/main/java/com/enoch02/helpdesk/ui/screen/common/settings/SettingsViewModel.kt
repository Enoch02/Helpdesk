package com.enoch02.helpdesk.ui.screen.common.settings

import android.net.Uri
import androidx.lifecycle.ViewModel
import com.enoch02.helpdesk.data.remote.repository.auth.FirebaseAuthRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: FirebaseAuthRepository) :
    ViewModel() {

    fun getUserInfo(): Triple<String?, String?, Uri?> {
        val user = repository.getUser()

        return Triple(
            user?.displayName,
            user?.email,
            user?.photoUrl,
        )
    }

    fun signOut() = repository.signOut()
}