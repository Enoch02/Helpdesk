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
                    val messages = chatObj.messages

                    if (messages?.isNotEmpty() == true) {
                        val mostRecentMsg = messages.last()

                        if (mostRecentMsg.sentBy != auth.currentUser?.uid.toString()) {
                            if (mostRecentMsg.read == false) {
                                sendNotification(
                                    context,
                                    title = mostRecentMsg.sentBy.toString(),
                                    message = mostRecentMsg.messageText.toString()
                                )
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
        title: String = "New Message",
        message: String = "You might have new messages",
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
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.ic_launcher_foreground)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(NOTIFICATION_ID, notification)
    }

    companion object {
        private const val NOTIFICATION_ID = 1001
    }
}