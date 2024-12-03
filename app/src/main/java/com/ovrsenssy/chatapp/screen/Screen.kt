package com.ovrsenssy.chatapp.screen

sealed class Screen(val route:String){
    object SplashScreen : Screen("splash")
    object LoginScreen:Screen("loginscreen")
    object SignupScreen:Screen("signupscreen")
    object ChatRoomsScreen:Screen("chatroomscreen")
    object ChatScreen:Screen("chatscreen")
}