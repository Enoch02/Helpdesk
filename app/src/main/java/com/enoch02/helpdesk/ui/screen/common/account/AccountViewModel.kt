package com.enoch02.helpdesk.ui.screen.common.account

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.enoch02.helpdesk.data.remote.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {
    var showProfile by mutableStateOf(true)
    var currentImage by mutableStateOf<Uri?>(null)
    var currentName by mutableStateOf("User")

    init {
        currentImage = getProfilePicture()
        currentName = getDisplayName() ?: "User"
    }

    fun getProfilePicture() = repository.getUser()?.photoUrl

    fun getDisplayName() = repository.getUser()?.displayName

    fun toggleShowProfile() {
        showProfile = !showProfile
    }

    fun updateCurrentImage(newImage: Uri?) {
        currentImage = newImage
    }

    fun updateCurrentName(newName: String) {
        currentName = newName
    }

    fun updateUserInfo() {
        repository.updateUserInfo(name = currentName, currentImage)
    }
}