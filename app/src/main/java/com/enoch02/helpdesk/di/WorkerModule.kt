package com.enoch02.helpdesk.di

import android.content.Context
import com.enoch02.helpdesk.data.local.repository.MessageUpdatesRepoImpl
import com.enoch02.helpdesk.data.local.repository.MessageUpdatesRepository
import dagger.Module
import dagger.Provides
import dagger.hilt.InstallIn
import dagger.hilt.android.qualifiers.ApplicationContext
import dagger.hilt.components.SingletonComponent
import javax.inject.Singleton

@Module
@InstallIn(SingletonComponent::class)
object WorkerModule {

    @Provides
    @Singleton
    fun provideMessageUpdateRepo(@ApplicationContext context: Context): MessageUpdatesRepository {
        return MessageUpdatesRepoImpl(context)
    }
}