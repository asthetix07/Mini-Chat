package com.ovrsenssy.chatapp

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModelProvider
import com.ovrsenssy.chatapp.data.UserRepository
import com.ovrsenssy.chatapp.viewmodel.AuthViewModel
import com.ovrsenssy.chatapp.viewmodel.AuthViewModelFactory
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import androidx.navigation.compose.rememberNavController
import com.ovrsenssy.chatapp.data.MessageRepository
import com.ovrsenssy.chatapp.data.RoomRepository
import com.ovrsenssy.chatapp.screen.NavigationGraph
import com.ovrsenssy.chatapp.ui.theme.ChatAppTheme
import com.ovrsenssy.chatapp.viewmodel.MessageViewModelFactory
import com.ovrsenssy.chatapp.viewmodel.RoomViewModelFactory

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        // Initialize Firebase instances
        val firebaseAuth = FirebaseAuth.getInstance()
        val firebaseFirestore = FirebaseFirestore.getInstance()

        // Initialize the UserRepository with Firebase instances
        val userRepository = UserRepository(firebaseAuth, firebaseFirestore)

        // Initialize the MessageRepository with the Firestore instance
        val messageRepository = MessageRepository(firebaseFirestore)

        // Create the MessageViewModelFactory and pass the repositories as parameters
        val messageViewModelFactory = MessageViewModelFactory(messageRepository, userRepository)

        // Create the RoomRepository and RoomViewModelFactory
        val roomRepository = RoomRepository(firebaseFirestore)
        val roomViewModelFactory = RoomViewModelFactory(roomRepository)

        // Create the AuthViewModelFactory
        val authViewModelFactory = AuthViewModelFactory(userRepository)

        // Create the AuthViewModel instance using the factory
        val authViewModel: AuthViewModel = ViewModelProvider(this, authViewModelFactory).get(AuthViewModel::class.java)

        enableEdgeToEdge()
        setContent {
            val navController = rememberNavController()
            ChatAppTheme {
                Surface(modifier = Modifier.fillMaxSize(), color = MaterialTheme.colorScheme.background) {
                    // Pass the factory to NavigationGraph
                    NavigationGraph(
                        navController = navController,
                        authViewModel = authViewModel,
                        messageViewModelFactory = messageViewModelFactory,
                        roomViewModelFactory = roomViewModelFactory  // Pass the roomViewModelFactory
                    )
                }
            }
        }
    }
}



