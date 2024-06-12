package com.enoch02.helpdesk.ui.screen.common.chat

import android.net.Uri
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateListOf
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.setValue
import androidx.lifecycle.ViewModel
import com.enoch02.helpdesk.ui.screen.common.chat.component.BubbleOwner
import com.enoch02.helpdesk.ui.screen.common.chat.component.BubbleType
import com.enoch02.helpdesk.ui.screen.common.chat.component.Message

class ChatViewModel : ViewModel() {
    var message by mutableStateOf("")
    var temp = mutableStateListOf<Message>()
    val selectedAttachments = mutableStateListOf<Uri>()

    init {
        for (i in 0..30) {
            temp.add(
                Message(
                    text = "Message $i",
                    owner = if (i % 2 == 0) BubbleOwner.SENDER else BubbleOwner.RECEIVER,
                    type = BubbleType.TEXT,
                    url = ""
                )
            )
        }
    }

    fun updateMessage(newMessage: String) {
        message = newMessage
    }

    //TODO: more demos
    fun sendMessage(): Result<Unit> {
        if (message.isEmpty() && selectedAttachments.isEmpty()) {
            return Result.failure(Exception("Enter some text"))
        }

        if (selectedAttachments.isEmpty()) {
            temp.add(
                Message(
                    text = message,
                    owner = BubbleOwner.RECEIVER,
                    type = BubbleType.TEXT,
                    url = ""
                )
            )
        } else {
            selectedAttachments.forEach {
                temp.add(
                    Message(
                        text = "",
                        owner = BubbleOwner.RECEIVER,
                        type = BubbleType.IMAGE,
                        url = it.toString()
                    )
                )
            }
        }
        message = ""
        selectedAttachments.clear()

        return Result.success(Unit)
    }

    fun removeAttachment(index: Int) {
        selectedAttachments.removeAt(index)
    }
}