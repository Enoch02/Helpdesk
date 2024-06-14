package com.enoch02.helpdesk.di

import com.enoch02.helpdesk.data.remote.repository.FirebaseRepository
import com.enoch02.helpdesk.data.remote.repository.FirebaseRepositoryImpl
import com.google.firebase.auth.FirebaseAuth
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object FirebaseModule {
    @Provides
    @Singleton
    fun provideFirebaseAuth() = FirebaseAuth.getInstance()

    @Provides
    @Singleton
    fun provideFirebaseRepository(
        firebaseAuth: FirebaseAuth,
        //db: FirebaseFirestore
    ): FirebaseRepository {
        return FirebaseRepositoryImpl(firebaseAuth/*, db*/)
    }
}