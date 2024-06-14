package com.enoch02.helpdesk.ui.screen.common.account

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.enoch02.helpdesk.data.remote.model.UserData
import com.enoch02.helpdesk.data.remote.repository.auth.FirebaseAuthRepository
import com.enoch02.helpdesk.data.remote.repository.cloud_storage.CloudStorageRepository
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import javax.inject.Inject

@HiltViewModel
class AccountViewModel @Inject constructor(
    private val authRepository: FirebaseAuthRepository,
    private val cloudStorageRepository: CloudStorageRepository,
    private val firestoreRepository: FirestoreRepository
) :
    ViewModel() {
    var showProfile by mutableStateOf(true)
    var currentImage by mutableStateOf<Uri?>(null)
    private var currentName by mutableStateOf("") // For updating the user's name
    var userData by mutableStateOf(UserData(displayName = null))

    init {
        getProfilePicture()
        getUserData()
    }

    fun getUserData() {
        viewModelScope.launch(Dispatchers.IO) {
            firestoreRepository.getUserData(authRepository.getUID())
                .onSuccess {
                    if (it != null) {
                        userData = it
                        currentName = it.displayName ?: "User"
                    }
                }
        }
    }

    //TODO: could take more data in the future
    private fun updateDisplayName() {
        viewModelScope.launch(Dispatchers.IO) {
            if (currentName != userData.displayName) {
                firestoreRepository.updateDisplayName(
                    uid = authRepository.getUID(),
                    newDisplayName = currentName
                )
            }
        }
    }

    fun getProfilePicture() {
        viewModelScope.launch(Dispatchers.IO) {
            cloudStorageRepository.getProfilePicture(authRepository.getUID()).onSuccess {
                currentImage = it
            }
        }
    }

    private fun updateProfilePicture() {
        if (currentImage != null) {
            viewModelScope.launch(Dispatchers.IO) {
                cloudStorageRepository.uploadProfilePicture(currentImage!!, authRepository.getUID())
                    .onSuccess {
                        getProfilePicture()
                    }
            }
        }
    }

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
        updateProfilePicture()
        updateDisplayName()
    }
}