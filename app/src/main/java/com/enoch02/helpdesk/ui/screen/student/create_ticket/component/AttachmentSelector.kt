package com.enoch02.helpdesk.ui.screen.student.create_ticket.component

import android.Manifest
import android.net.Uri
import android.os.Build
import android.widget.Toast
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.ExperimentalFoundationApi
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.lazy.grid.GridCells
import androidx.compose.foundation.lazy.grid.LazyHorizontalGrid
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Clear
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.snapshots.SnapshotStateList
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@OptIn(ExperimentalFoundationApi::class)
@Composable
fun AttachmentSelector(
    modifier: Modifier,
    selectedImages: SnapshotStateList<Uri>,
    onRemoveAttachmentAt: (index: Int) -> Unit
) {
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

    Column(
        modifier = modifier,
        content = {
            TextButton(
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
                    Text(text = "Select Attachments")
                }
            )

            Box(
                modifier = Modifier.then(if (selectedImages.size > 0) Modifier.height(120.dp) else Modifier),
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
                                                onClick = { onRemoveAttachmentAt(index) },
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
}
