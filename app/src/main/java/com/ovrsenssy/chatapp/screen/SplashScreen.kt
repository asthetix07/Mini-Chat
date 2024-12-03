package com.ovrsenssy.chatapp.screen

import androidx.compose.foundation.Image
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.res.painterResource
import androidx.navigation.NavController
import com.ovrsenssy.chatapp.R
import kotlinx.coroutines.delay

@Composable
fun SplashScreen(navController: NavController) {
    LaunchedEffect(Unit) {
        delay(1000) // 1 second delay
        navController.navigate(Screen.LoginScreen.route) {
            popUpTo("splash") { inclusive = true } // Remove splash from back stack
        }
    }

    Box(
        contentAlignment = Alignment.Center,
        modifier = Modifier.fillMaxSize()
    ) {
        Image(
            painter = painterResource(id = R.drawable.icons8_telegram_logo_375), // Replace with your logo resource ID
            contentDescription = "App Logo"
        )
    }
}
