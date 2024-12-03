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
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.automirrored.filled.KeyboardArrowRight
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.OutlinedTextFieldDefaults
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.livedata.observeAsState
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.BlurEffect
import androidx.compose.ui.graphics.Brush
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.graphics.graphicsLayer
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.colorResource
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.input.PasswordVisualTransformation
import androidx.compose.ui.unit.dp
import com.ovrsenssy.chatapp.R
import com.ovrsenssy.chatapp.viewmodel.AuthViewModel
import com.ovrsenssy.chatapp.data.Result



@Composable
fun SignUpScreen(authViewModel: AuthViewModel, onNavigateToLogin: () -> Unit, onNavigateToChatRoomList: () -> Unit) {
    var email by remember { mutableStateOf("") }
    var password by remember { mutableStateOf("") }
    var firstName by remember { mutableStateOf("") }
    var lastName by remember { mutableStateOf("") }
    var errorMessage by remember { mutableStateOf("") }
    var isLoading by remember { mutableStateOf(false) }

    // Observe the result of the sign-up operation
    val result = authViewModel.authResult.observeAsState().value


    result?.let { authResult ->
        when (authResult) {
            is Result.Success -> {
                // Handle success - clear fields and navigate if necessary
                email = ""
                password = ""
                firstName = ""
                lastName = ""
                errorMessage = ""
                onNavigateToChatRoomList()
            }
            is Result.Error -> {
                // Handle error - display error message
                errorMessage = authResult.exception.message ?: "An error occurred"
            }
        }
        // Update loading state after operation is complete
        isLoading = false
    }


    Box(modifier = Modifier.fillMaxSize()

    ) {

        Image(
            painter = painterResource(id = R.drawable.image__2_),
            contentDescription = null,
            contentScale = ContentScale.Crop,
            modifier = Modifier.fillMaxSize()
        )

        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp),
            verticalArrangement = Arrangement.Center,
            horizontalAlignment = Alignment.CenterHorizontally
        ) {

            OutlinedTextField(
                value = email,
                onValueChange = { email = it },
                singleLine = true,
                label = { Text("Email") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_email_24),
                        contentDescription = "Email Icon",
                        tint = Color.Unspecified
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black,unfocusedTextColor = Color.Black, unfocusedLabelColor = Color.Black)
            )

            OutlinedTextField(
                value = password,
                onValueChange = { password = it },
                singleLine = true,
                label = { Text("Password") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                visualTransformation = PasswordVisualTransformation(),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_password_24),
                        contentDescription = "Email Icon",
                        tint = Color.Unspecified
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black,unfocusedTextColor = Color.Black, unfocusedLabelColor = Color.Black)
            )

            OutlinedTextField(
                value = firstName,
                onValueChange = { firstName = it },
                singleLine = true,
                label = { Text("First Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_abc_24),
                        contentDescription = "Email Icon",
                        tint = Color.Unspecified
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black,unfocusedTextColor = Color.Black, unfocusedLabelColor = Color.Black)
            )

            OutlinedTextField(
                value = lastName,
                onValueChange = { lastName = it },
                singleLine = true,
                label = { Text("Last Name") },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(8.dp),
                leadingIcon = {
                    Icon(
                        painter = painterResource(id = R.drawable.baseline_abc_24),
                        contentDescription = "Email Icon",
                        tint = Color.Unspecified
                    )
                },
                colors = OutlinedTextFieldDefaults.colors(focusedTextColor = Color.Black,unfocusedTextColor = Color.Black, unfocusedLabelColor = Color.Black)
            )

// Display error message if any
            if (errorMessage.isNotEmpty()) {
                Text(
                    text = errorMessage,
                    color = Color.Red,
                    modifier = Modifier.padding(8.dp)
                )
            }

//Display Loading
            if (isLoading) {
                // Show a loading indicator while sign-up is in progress
                Text("Signing up...", modifier = Modifier.padding(8.dp))
            }
            Spacer(modifier = Modifier.height(16.dp))

            Button(
                onClick = {
                    isLoading = true
                    authViewModel.signUp(email, password, firstName, lastName)
                },
                modifier = Modifier
                    .fillMaxWidth()
                    .padding(vertical = 8.dp, horizontal = 50.dp)
                    .background(
                        Brush.verticalGradient(
                            colors = listOf(
                                Color(0xFFBBDEFB),
                                Color(0xFF2196F3)
                            )
                        ),
                        shape = RoundedCornerShape(50)
                    ),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color.Transparent // Ensure the button uses a transparent background
                )
            ) {
                Text(
                    "Sign Up",
                    color = Color.Unspecified // Adjust the text color for contrast
                )
            }

            Spacer(modifier = Modifier.height(1.dp))

            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center
            ) {
                Text("Already have an account? ", color = Color.White)
                TextButton(onClick = { onNavigateToLogin() }) {
                    Row(verticalAlignment = Alignment.CenterVertically) {
                        Text("Sign in", color = Color.White)
                        Icon(
                            imageVector = Icons.AutoMirrored.Filled.KeyboardArrowRight,
                            contentDescription = "Plan arrow",
                            tint = Color.Unspecified
                        )
                    }
                }
            }

        }
    }
}