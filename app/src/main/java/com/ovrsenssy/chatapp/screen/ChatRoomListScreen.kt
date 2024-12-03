package com.ovrsenssy.chatapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.lazy.LazyColumn
import androidx.compose.foundation.lazy.items
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.OutlinedButton
import androidx.compose.material3.OutlinedTextField
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import com.ovrsenssy.chatapp.R
import com.ovrsenssy.chatapp.data.Room
import com.ovrsenssy.chatapp.viewmodel.RoomViewModel


@Composable
fun ChatRoomsListScreen(roomViewModel: RoomViewModel, onJoinClicked: (Room) -> Unit
) {

    val rooms by roomViewModel.rooms.observeAsState(emptyList())
    val isLoading by roomViewModel.isLoading.observeAsState(false)
    val errorMessage by roomViewModel.errorMessage.observeAsState()
    var showDialog by remember { mutableStateOf(false) }
    var name by remember { mutableStateOf("") }

    Box(
        modifier = Modifier
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
            modifier = Modifier.fillMaxSize().padding(top = 25.dp, bottom = 35.dp)
        ) {
            Text(
                modifier = Modifier.align(Alignment.CenterHorizontally),
                text = " <-- Chat Rooms --> ",
                fontSize = 20.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(10.dp))

            when {

                isLoading -> Text(
                    "Loading...",
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                errorMessage != null -> Text(
                    "Error: $errorMessage",
                    color = Color.Red,
                    modifier = Modifier.align(Alignment.CenterHorizontally)
                )

                rooms.isEmpty() -> Text(
                    "No rooms available. Create one to get started!",
                    modifier = Modifier.align(Alignment.CenterHorizontally),
                    fontSize = 16.sp
                )

                else -> LazyColumn(
                    modifier = Modifier.weight(1f) // Ensures the LazyColumn takes up available space
                ) {
                    items(rooms) { room ->
                        RoomItem(room = room, onJoinClicked = { onJoinClicked(room) })
                    }
                }
            }
        }

        Button(
            onClick = { showDialog = true },
            modifier = Modifier
                .fillMaxWidth()
                .align(Alignment.BottomCenter)
                .padding(vertical = 50.dp, horizontal = 50.dp)
                .background(
                    Brush.verticalGradient(colors = listOf(Color(0xFFBBDEFB), Color(0xFF2196F3))),
                    shape = RoundedCornerShape(50)
                ),
            enabled = !isLoading,
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
        ) {
            Text("Create Room")
        }
    }
        if (showDialog) {
            AlertDialog(
                onDismissRequest = { showDialog = false },
                title = { Text("Create room") },
                text = {
                    OutlinedTextField(
                        label = { Text("Enter a name") },
                        value = name,
                        onValueChange = { name = it },
                        singleLine = true,
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(4.dp)
                    )
                },
                confirmButton = {
                    Row(
                        modifier = Modifier
                            .fillMaxWidth()
                            .padding(8.dp),
                        horizontalArrangement = Arrangement.SpaceBetween
                    ) {
                        Button(elevation = (ButtonDefaults.buttonElevation(25.dp)),
                            onClick = {
                                if (name.isNotBlank()) {
                                    roomViewModel.createRoom(name)
                                    name = ""
                                    showDialog = false
                                }
                            }
                        ) {
                            Text("Add")
                        }
                        Button(elevation = (ButtonDefaults.buttonElevation(25.dp)),
                            onClick = { showDialog = false }
                        ) {
                            Text("Cancel")
                        }
                    }
                }
            )
        }
    //}
}


@Composable
fun RoomItem(room: Room, onJoinClicked: (Room)->Unit) {
    Row(
        modifier = Modifier
            .fillMaxWidth()
            .padding(8.dp),
        horizontalArrangement = Arrangement.SpaceBetween,
        verticalAlignment = Alignment.CenterVertically
    ) {
        Text(text = room.name, fontSize = 16.sp, fontWeight = FontWeight.Normal)
        Button(modifier = Modifier.background(
                Brush.verticalGradient(colors = listOf(Color(0xFFBBDEFB), Color(0xFF2196F3))),shape = RoundedCornerShape(50)),
            elevation = (ButtonDefaults.buttonElevation(25.dp)),
            onClick = { onJoinClicked(room) },colors = ButtonDefaults.buttonColors(
                containerColor = Color.Transparent
            )
        ) {
            Text("Join", color = Color.Unspecified)
        }
    }
}
