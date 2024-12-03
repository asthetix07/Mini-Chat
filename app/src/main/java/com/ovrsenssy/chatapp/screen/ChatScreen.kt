package com.ovrsenssy.chatapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.itemsIndexed
import androidx.compose.foundation.lazy.rememberLazyListState
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.Send
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.TextStyle
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.viewmodel.compose.viewModel
import com.ovrsenssy.chatapp.R
import com.ovrsenssy.chatapp.viewmodel.MessageViewModel


@Composable
fun ChatScreen(roomId: String, messageViewModel: MessageViewModel = viewModel()) {

    val message by messageViewModel.messages.observeAsState(emptyList())
    messageViewModel.setRoomId(roomId)
    val text = remember { mutableStateOf("") }

    // Remember LazyListState to control scrolling
    val listState = rememberLazyListState()


    Box(modifier = Modifier
        .fillMaxSize()
        //.padding(top = 25.dp, bottom = 35.dp)
    ) {

        Image(
            painter = painterResource(id = R.drawable.image__1_),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(top = 25.dp, bottom = 35.dp)
        ) {
            // Display the chat messages
            LazyColumn(
                modifier = Modifier.weight(1f),
                state = listState // Bind LazyColumn to the remembered LazyListState
            ) {
                itemsIndexed(message) { index, message ->
                    val isSentByCurrentUser =
                        message.senderId == messageViewModel.currentUser.value?.email
                    ChatMessageItem(message = message.copy(isSentByCurrentUser = isSentByCurrentUser))
                }
            }

            // Automatically scroll to the bottom when a new message arrives
            LaunchedEffect(message) {
                if (message.isNotEmpty()) {
                    listState.animateScrollToItem(message.size - 1)
                }
            }

            // Chat input field and send icon
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                verticalAlignment = Alignment.CenterVertically
            ) {
                OutlinedTextField(
                    value = text.value,
                    label = { Text("Write something ...") },
                    onValueChange = { text.value = it },
                    textStyle = TextStyle.Default.copy(fontSize = 16.sp),
                    modifier = Modifier
                        .weight(1f)
                        .padding(8.dp),
                    colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.White,unfocusedTextColor = Color.Black,
                        unfocusedLabelColor = Color.White, focusedLabelColor = Color.White, focusedBorderColor = Color.White)
                )

                IconButton(
                    onClick = {
                        // Send the message when the icon is clicked
                        if (text.value.isNotEmpty()) {
                            messageViewModel.sendMessage(text.value.trim())
                            text.value = ""
                        }
                        messageViewModel.loadMessages()
                    }
                ) {
                    Icon(
                        imageVector = Icons.AutoMirrored.Filled.Send,
                        contentDescription = "Send",
                        tint = Color.White
                    )
                }
            }
        }
    }
}
