package com.enoch02.helpdesk.ui.screen.common.chat

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedContent
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.imePadding
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material.icons.filled.Attachment
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.ExperimentalMaterial3Api
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.material3.TopAppBar
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.SideEffect
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalSoftwareKeyboardController
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.hilt.navigation.compose.hiltViewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.enoch02.helpdesk.data.remote.model.MessageType
import com.enoch02.helpdesk.ui.screen.common.chat.component.BubbleOwner
import com.enoch02.helpdesk.ui.screen.common.chat.component.ChatBubble
import com.enoch02.helpdesk.ui.screen.common.chat.component.ImageBubble
import kotlinx.coroutines.launch

@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ChatScreen(
    chatID: String,
    navController: NavController,
    viewModel: ChatViewModel = hiltViewModel(),
) {
    val message = viewModel.message
    val selectedImages = viewModel.selectedAttachments
    val recepientName = viewModel.recepientName
    val recipientPic = viewModel.recipientProfilePic

    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val context = LocalContext.current
    val keyboardController = LocalSoftwareKeyboardController.current
    val getContent =
        rememberLauncherForActivityResult(ActivityResultContracts.GetMultipleContents()) { uris ->
            selectedImages.addAll(uris)
        }
    val requestPermission =
        rememberLauncherForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissionStatusMap ->
            if (permissionStatusMap.values.all { true }) {
                getContent.launch("image/*")
            } else {
                //TODO: replace with snack bar.
                Toast.makeText(
                    context,
                    "This permission is needed to select attachments",
                    Toast.LENGTH_SHORT
                ).show()
            }
        }

    LaunchedEffect(
        key1 = Unit,
        block = {
            viewModel.updateChat(chatID)
        }
    )

    LaunchedEffect(
        key1 = viewModel.chat,
        block = {
            if (!viewModel.chat?.messages.isNullOrEmpty()) {
                listState.animateScrollToItem((viewModel.chat?.messages?.size ?: 1) - 1)
            }
        }
    )

    SideEffect {
        viewModel.getChat(cid = chatID)
        viewModel.getRecepientName()
    }

    Scaffold(
        topBar = {
            TopAppBar(
                title = {
                    Text(text = recepientName)
                },
                navigationIcon = {
                    Row(
                        verticalAlignment = Alignment.CenterVertically,
                        content = {
                            IconButton(
                                onClick = { navController.popBackStack() },
                                content = {
                                    Icon(
                                        imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                        contentDescription = null
                                    )
                                }
                            )

                            AnimatedContent(
                                targetState = recipientPic == null,
                                content = { state ->
                                    when (state) {
                                        true -> {
                                            Icon(
                                                imageVector = Icons.Default.AccountCircle,
                                                contentDescription = null
                                            )
                                        }

                                        false -> {
                                            AsyncImage(
                                                model = recipientPic,
                                                contentDescription = null,
                                                contentScale = ContentScale.Crop,
                                                modifier = Modifier
                                                    .size(24.dp)
                                                    .clip(CircleShape)
                                            )
                                        }
                                    }
                                },
                                label = "profile pic anim"
                            )
                        }
                    )
                }
            )
        },
        content = { paddingValues ->
            LazyColumn(
                state = listState,
                content = {
                    viewModel.chat?.messages?.let { messages ->
                        items(
                            count = messages.size,
                            itemContent = { index ->
                                val item = messages[index]
                                val owner =
                                    if (item.sentBy == viewModel.getUID()) BubbleOwner.LOCAL else BubbleOwner.REMOTE

                                when (item.type) {
                                    MessageType.TEXT -> {
                                        ChatBubble(
                                            content = item.messageText.toString(),
                                            owner = owner
                                        )
                                    }

                                    MessageType.IMAGE -> {
                                        ImageBubble(
                                            content = viewModel.getPictureAt(index),
                                            owner = owner
                                        )
                                    }

                                    MessageType.IMAGE_AND_TEXT -> {
                                        ImageBubble(
                                            content = viewModel.getPictureAt(index),
                                            text = item.messageText.toString(),
                                            owner = owner
                                        )
                                    }

                                    null -> {

                                    }
                                }
                            }
                        )
                    }
                },
                modifier = Modifier
                    .padding(paddingValues)
            )
        },
        bottomBar = {
            Column {
                AnimatedVisibility(
                    visible = selectedImages.isNotEmpty(),
                    content = {
                        // TODO: move to own composable
                        Box(
                            modifier = Modifier.height(120.dp),
                            content = {
                                LazyHorizontalGrid(
                                    rows = GridCells.Fixed(1),
                                    content = {
                                        items(
                                            count = selectedImages.size,
                                            itemContent = { index ->
                                                Box(
                                                    modifier = Modifier.animateItemPlacement(),
                                                    contentAlignment = Alignment.TopEnd,
                                                    content = {
                                                        AsyncImage(
                                                            model = selectedImages[index],
                                                            contentDescription = null,
                                                            modifier = Modifier
                                                                .padding(4.dp)
                                                                .size(120.dp)
                                                        )

                                                        IconButton(
                                                            onClick = {
                                                                viewModel.removeAttachment(
                                                                    index
                                                                )
                                                            },
                                                            content = {
                                                                Icon(
                                                                    imageVector = Icons.Default.Clear,
                                                                    contentDescription = "Remove attachment"
                                                                )
                                                            }
                                                        )
                                                    }
                                                )
                                            }
                                        )
                                    },
                                    modifier = Modifier.fillMaxWidth()
                                )
                            }
                        )
                    }
                )

                OutlinedTextField(
                    value = message,
                    onValueChange = { viewModel.updateMessage(it) },
                    modifier = Modifier
                        .fillMaxWidth()
                        .padding(8.dp),
                    keyboardOptions = KeyboardOptions(
                        capitalization = KeyboardCapitalization.Sentences,
                        keyboardType = KeyboardType.Text,
                        autoCorrect = true
                    ),
                    maxLines = 5,
                    leadingIcon = {
                        IconButton(
                            onClick = {
                                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.UPSIDE_DOWN_CAKE) {
                                    requestPermission.launch(
                                        arrayOf(
                                            Manifest.permission.READ_MEDIA_IMAGES,
                                            Manifest.permission.READ_MEDIA_VISUAL_USER_SELECTED
                                        )
                                    )
                                } else if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.TIRAMISU) {
                                    requestPermission.launch(arrayOf(Manifest.permission.READ_MEDIA_IMAGES))
                                } else {
                                    requestPermission.launch(arrayOf(Manifest.permission.READ_EXTERNAL_STORAGE))
                                }
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.Default.Attachment,
                                    contentDescription = null
                                )
                            }
                        )
                    },
                    trailingIcon = {
                        IconButton(
                            onClick = {
                                keyboardController?.hide()
                                viewModel.sendMessage(context = context, cid = chatID)
                                    .onSuccess {
                                        scope.launch {
                                            if (!viewModel.chat?.messages.isNullOrEmpty()) {
                                                listState.animateScrollToItem(
                                                    (viewModel.chat?.messages?.size ?: 1) - 1
                                                )
                                            }
                                        }
                                    }
                            },
                            content = {
                                Icon(
                                    imageVector = Icons.AutoMirrored.Default.Send,
                                    contentDescription = null
                                )
                            }
                        )
                    }
                )
            }
        },
        modifier = Modifier.imePadding()
    )
}