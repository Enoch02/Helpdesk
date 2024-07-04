package com.enoch02.helpdesk.ui.screen.common.messages

import androidx.lifecycle.ViewModel
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class MessagesViewModel @Inject constructor(firestoreRepository: FirestoreRepository) : ViewModel() {

}
