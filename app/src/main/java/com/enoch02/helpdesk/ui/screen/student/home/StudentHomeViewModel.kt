package com.enoch02.helpdesk.ui.screen.student.home

import androidx.lifecycle.ViewModel
import com.enoch02.helpdesk.data.remote.repository.FirebaseRepository
import dagger.hilt.android.lifecycle.HiltViewModel
import javax.inject.Inject

@HiltViewModel
class StudentHomeViewModel @Inject constructor(private val repository: FirebaseRepository) :
    ViewModel() {

    fun getDisplayName() = repository.getUser()?.displayName
}