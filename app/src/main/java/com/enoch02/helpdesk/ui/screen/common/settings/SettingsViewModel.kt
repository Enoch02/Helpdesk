package com.enoch02.helpdesk.ui.screen.common.settings

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.enoch02.helpdesk.data.remote.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class SettingsViewModel @Inject constructor(private val repository: FirebaseRepository) :
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