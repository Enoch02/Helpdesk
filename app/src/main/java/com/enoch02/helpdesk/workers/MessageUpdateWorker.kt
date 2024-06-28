package com.enoch02.helpdesk.workers

import android.app.Notification
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import androidx.core.app.NotificationCompat
import androidx.work.CoroutineWorker
import androidx.work.ForegroundInfo
import androidx.work.WorkerParameters
import androidx.work.workDataOf
import com.enoch02.helpdesk.MainActivity
import com.enoch02.helpdesk.R
import com.enoch02.helpdesk.data.remote.model.Chat
import com.enoch02.helpdesk.data.remote.repository.firestore_db.CHATS_COLLECTION_NAME
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await
import java.util.Date

class MessageUpdateWorker(
    private val context: Context,
    private val workerParameters: WorkerParameters,
) :
    CoroutineWorker(context, workerParameters) {
    private val db = FirebaseFirestore.getInstance()
    private val auth = FirebaseAuth.getInstance()

    override suspend fun doWork(): Result {
        val collectionPath = CHATS_COLLECTION_NAME

        try {
            val documentSnapshot = db.collection(collectionPath).get().await()

            if (documentSnapshot.isEmpty) {
                return Result.retry()
            } else {
                documentSnapshot.forEach { document ->
                    val chatObj = document.toObject(Chat::class.java)

                    // TODO: fix
                    if (chatObj.members?.userID.toString() == auth.currentUser?.uid.toString()) {
                        val messages = chatObj.messages

                        if (messages?.isNotEmpty() == true) {
                            val mostRecentMsg = messages.last()

                            // check if most recent message is not from this user
                            if (mostRecentMsg.sentBy != auth.currentUser?.uid.toString()) {
                                val messageDate = mostRecentMsg.sentAt

                                messageDate?.let {
                                    if (isWithin30MinutesBefore(it)) {
                                        sendNotification(context)
                                        return@forEach
                                    }
                                }
                            }
                        }
                    }
                }
            }
        } catch (e: Exception) {
            return Result.failure(workDataOf("exception" to e.toString()))
        }

        return Result.success()
    }

    override suspend fun getForegroundInfo(): ForegroundInfo {
        return ForegroundInfo(
            NOTIFICATION_ID, createNotification()
        )
    }

    private fun createNotification(): Notification {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager

        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "message_updates",
                "Message Updates",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        return NotificationCompat.Builder(context, "message_updates")
            .setContentTitle("New Message")
            .setContentText("You might have new messages..")
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setAutoCancel(true)
            .build()
    }

    private fun sendNotification(
        context: Context,
        message: String = "You might have new messages..",
    ) {
        val notificationManager =
            context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val intent = Intent(context, MainActivity::class.java).apply {
            flags = Intent.FLAG_ACTIVITY_NEW_TASK or Intent.FLAG_ACTIVITY_CLEAR_TASK
        }
        val pendingIntent: PendingIntent = PendingIntent.getActivity(
            context, 0, intent,
            PendingIntent.FLAG_IMMUTABLE
        )


        if (android.os.Build.VERSION.SDK_INT >= android.os.Build.VERSION_CODES.O) {
            val channel = NotificationChannel(
                "message_updates",
                "Message Updates",
                NotificationManager.IMPORTANCE_DEFAULT
            )
            notificationManager.createNotificationChannel(channel)
        }

        val notification = NotificationCompat.Builder(context, "message_updates")
            .setContentTitle("New Message")
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val NOTIFICATION_ID = 1001

        fun isWithin30MinutesBefore(date1: Date, date2: Date = Date()): Boolean {
            // Get the time in milliseconds for both dates
            val time1 = date1.time
            val time2 = date2.time

            val timeDifference = time2 - time1

            // Convert 30 minutes to milliseconds
            val thirtyMinutesInMillis = 30 * 60 * 1000

            // Check if date1 is within 30 minutes before date2
            return timeDifference in 0..thirtyMinutesInMillis
        }
    }
}