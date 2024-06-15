package com.enoch02.helpdesk.ui.screen.common.account.component

import android.net.Uri
import androidx.compose.animation.AnimatedVisibility
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material.icons.filled.AccountCircle
import androidx.compose.material3.Card
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.tooling.preview.Preview
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage

@Composable
fun Profile(
    displayName: String?,
    profilePic: Uri?,
    onDisplayNameClick: () -> Unit,
    modifier: Modifier,
) {
    // TODO: Add some stats
    LazyColumn(
        modifier = modifier,
        content = {
            item {
                Card(
                    modifier = Modifier.padding(8.dp),
                    content = {
                        AnimatedVisibility(
                            visible = profilePic == null,
                            content = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp),
                                    content = {
                                        Icon(
                                            imageVector = Icons.Default.AccountCircle,
                                            contentDescription = null,
                                            modifier = Modifier.size(128.dp)
                                        )
                                    },
                                    contentAlignment = Alignment.Center
                                )
                            }
                        )

                        AnimatedVisibility(
                            visible = profilePic != null,
                            content = {
                                Box(
                                    modifier = Modifier
                                        .fillMaxWidth()
                                        .padding(4.dp),
                                    content = {
                                        AsyncImage(
                                            model = profilePic,
                                            contentDescription = null,
                                            contentScale = ContentScale.Crop,
                                            modifier = Modifier
                                                .size(128.dp)
                                                .clip(CircleShape)
                                        )
                                    },
                                    contentAlignment = Alignment.Center
                                )
                            }
                        )

                        Spacer(modifier = Modifier.height(8.dp))


                        Box(
                            modifier = Modifier
                                .fillMaxWidth()
                                .padding(4.dp),
                            content = {
                                TextButton(
                                    onClick = {
                                        onDisplayNameClick()
                                    },
                                    content = {
                                        Text(text = displayName ?: "")
                                        Spacer(modifier = Modifier.width(4.dp))
                                        Icon(
                                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                                            contentDescription = null
                                        )
                                    }
                                )
                            },
                            contentAlignment = Alignment.Center
                        )
                    }
                )
            }

            item {
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .height(200.dp),  //TODO: remove
                    content = {

                    }
                )
            }

            item {
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .height(200.dp),  //TODO: remove
                    content = {

                    }
                )
            }

            item {
                Card(
                    modifier = Modifier
                        .padding(8.dp)
                        .fillMaxWidth()
                        .height(200.dp),  //TODO: remove
                    content = {

                    }
                )
            }
        }
    )
}

@Preview(showBackground = true, showSystemUi = true)
@Composable
private fun Preview() {
    Profile(
        displayName = "Enoch",
        profilePic = null,
        onDisplayNameClick = {},
        modifier = Modifier.fillMaxSize()
    )
}