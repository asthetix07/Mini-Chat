package com.ovrsenssy.chatapp.screen

import androidx.compose.runtime.Composable
import androidx.lifecycle.viewmodel.compose.viewModel
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import com.ovrsenssy.chatapp.viewmodel.AuthViewModel
import com.ovrsenssy.chatapp.viewmodel.MessageViewModel
import com.ovrsenssy.chatapp.viewmodel.MessageViewModelFactory
import com.ovrsenssy.chatapp.viewmodel.RoomViewModel
import com.ovrsenssy.chatapp.viewmodel.RoomViewModelFactory

@Composable
fun NavigationGraph(
    navController: NavHostController,
    authViewModel: AuthViewModel,
    roomViewModelFactory: RoomViewModelFactory,
    messageViewModelFactory: MessageViewModelFactory // Pass the factory
) {
    NavHost(
        navController = navController,
        startDestination = Screen.SplashScreen.route // Set SplashScreen as the start destination
    ) {
        composable(Screen.SplashScreen.route) {
            SplashScreen(navController = navController)
        }

        composable(Screen.SignupScreen.route) {
            SignUpScreen(
                authViewModel = authViewModel,
                onNavigateToLogin = { navController.navigate(Screen.LoginScreen.route) },
                onNavigateToChatRoomList = {
                    navController.navigate(Screen.ChatRoomsScreen.route) {
                        popUpTo(0) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.LoginScreen.route) {
            LoginScreen(
                authViewModel = authViewModel,
                onNavigateToSignUp = { navController.navigate(Screen.SignupScreen.route) },
                onSignInSuccess = {
                    navController.navigate(Screen.ChatRoomsScreen.route) {
                        popUpTo(Screen.LoginScreen.route) { inclusive = true }
                    }
                }
            )
        }

        composable(Screen.ChatRoomsScreen.route) {
            val roomViewModel: RoomViewModel = viewModel(factory = roomViewModelFactory)
            ChatRoomsListScreen(roomViewModel = roomViewModel) {
                navController.navigate("${Screen.ChatScreen.route}/${it.id}")
            }
        }

        composable("${Screen.ChatScreen.route}/{roomId}") { backStackEntry ->
            val roomId: String = backStackEntry.arguments?.getString("roomId") ?: ""

            // Use the factory to create the MessageViewModel instance
            val messageViewModel: MessageViewModel = viewModel(factory = messageViewModelFactory)

            // Pass messageViewModel to ChatScreen
            ChatScreen(roomId = roomId, messageViewModel = messageViewModel)
        }
    }
}
