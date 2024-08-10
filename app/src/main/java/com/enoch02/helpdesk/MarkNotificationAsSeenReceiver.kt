package com.enoch02.helpdesk

import android.content.BroadcastReceiver
import android.content.Context
import android.content.Intent
import android.widget.Toast


class MarkNotificationAsSeenReceiver : BroadcastReceiver() {
    override fun onReceive(context: Context, intent: Intent?) {
        val action = intent?.action

        if (action == "MARK_SEEN") {
            // Handle button click here
            val message = intent.getStringExtra("message")
            Toast.makeText(context, "Button clicked: $message", Toast.LENGTH_SHORT).show()
        }
    }
}