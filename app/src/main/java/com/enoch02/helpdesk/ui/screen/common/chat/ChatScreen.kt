package com.enoch02.helpdesk.ui.screen.common.chat

import android.Manifest
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.foundation.text.KeyboardOptions
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.ArrowBack
import androidx.compose.material.icons.automirrored.filled.Send
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
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.text.input.KeyboardCapitalization
import androidx.compose.ui.text.input.KeyboardType
import androidx.compose.ui.unit.dp
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavController
import coil.compose.AsyncImage
import com.enoch02.helpdesk.ui.screen.common.chat.component.BubbleType
import com.enoch02.helpdesk.ui.screen.common.chat.component.ChatBubble
import com.enoch02.helpdesk.ui.screen.common.chat.component.ImageBubble
import kotlinx.coroutines.launch

//TODO: selected image previews??
@OptIn(ExperimentalMaterial3Api::class, ExperimentalFoundationApi::class)
@Composable
fun ChatScreen(
    navController: NavController,
    viewModel: ChatViewModel = viewModel()
) {
    val message = viewModel.message
    val temp = viewModel.temp
    val selectedImages = viewModel.selectedAttachments

    val scope = rememberCoroutineScope()
    val listState = rememberLazyListState()
    val context = LocalContext.current
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
            listState.scrollToItem(temp.size - 1)
        }
    )

    Scaffold(
        topBar = {
            TopAppBar(
                title = { Text(text = "Chat Name") },
                navigationIcon = {
                    IconButton(
                        onClick = { navController.popBackStack() },
                        content = {
                            Icon(
                                imageVector = Icons.AutoMirrored.Default.ArrowBack,
                                contentDescription = null
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
                    items(
                        count = temp.size,
                        itemContent = {
                            val item = temp[it]

                            when (item.type) {
                                BubbleType.TEXT -> {
                                    ChatBubble(
                                        content = item.text,
                                        owner = item.owner
                                    )
                                }

                                BubbleType.IMAGE -> {
                                    ImageBubble(content = item.url, owner = item.owner)
                                }
                            }
                        }
                    )
                },
                modifier = Modifier.padding(paddingValues)
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
                                                            onClick = { viewModel.removeAttachment(index) },
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
                                viewModel.sendMessage()
                                    .onSuccess {
                                        scope.launch {
                                            // TODO: replace with actual array of messages from the chat
                                            listState.scrollToItem(temp.size - 1)
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
        }
    )
}