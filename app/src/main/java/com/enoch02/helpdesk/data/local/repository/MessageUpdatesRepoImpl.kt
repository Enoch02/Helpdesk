package com.enoch02.helpdesk.data.local.repository

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.NetworkType
import androidx.work.OutOfQuotaPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import androidx.work.WorkRequest
import com.enoch02.helpdesk.workers.MessageUpdateWorker
import java.util.concurrent.TimeUnit

class MessageUpdatesRepoImpl(private val context: Context) : MessageUpdatesRepository {
    override fun checkForUpdates(interval: Long) {
        //TODO: Disabled for now
        /*val constraints = Constraints.Builder()
            .setRequiredNetworkType(NetworkType.CONNECTED)
            .build()

        val periodicWorkRequest =
            PeriodicWorkRequestBuilder<MessageUpdateWorker>(interval, TimeUnit.MILLISECONDS)
                .setConstraints(constraints)
                .setBackoffCriteria(
                    BackoffPolicy.LINEAR,
                    WorkRequest.MIN_BACKOFF_MILLIS,
                    TimeUnit.MILLISECONDS
                )
                .build()

        WorkManager.getInstance(context).enqueueUniquePeriodicWork(
            "message_update_check",
            ExistingPeriodicWorkPolicy.KEEP,
            periodicWorkRequest
        )*/
    }

    override fun cancelWork() {

    }
}