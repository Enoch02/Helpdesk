package com.enoch02.helpdesk.di

import com.enoch02.helpdesk.data.remote.repository.auth.FirebaseAuthRepository
import com.enoch02.helpdesk.data.remote.repository.auth.FirebaseAuthRepositoryImpl
import com.enoch02.helpdesk.data.remote.repository.cloud_storage.CloudStorageRepository
import com.enoch02.helpdesk.data.remote.repository.cloud_storage.CloudStorageRepositoryImpl
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepository
import com.enoch02.helpdesk.data.remote.repository.firestore_db.FirestoreRepositoryImpl
import com.google.firebase.Firebase
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import com.google.firebase.firestore.firestore
import com.google.firebase.storage.FirebaseStorage
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
    fun provideFirebaseStorage() = FirebaseStorage.getInstance()

    @Provides
    @Singleton
    fun provideFirestoreDatabase() = Firebase.firestore

    @Provides
    @Singleton
    fun provideFirebaseAuthRepository(firebaseAuth: FirebaseAuth): FirebaseAuthRepository =
        FirebaseAuthRepositoryImpl(firebaseAuth)


    @Provides
    @Singleton
    fun provideCloudStorageRepository(firebaseStorage: FirebaseStorage): CloudStorageRepository =
        CloudStorageRepositoryImpl(firebaseStorage)

    @Provides
    @Singleton
    fun provideRealtimeDatabaseRepository(firestoreDatabase: FirebaseFirestore): FirestoreRepository =
        FirestoreRepositoryImpl(firestoreDatabase)
}