package com.ovrsenssy.chatapp.data

import android.util.Log
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class UserRepository(
    private val auth: FirebaseAuth,
    private val firestore: FirebaseFirestore
) {

    suspend fun signUp(
        email: String,

        password: String,
        firstName: String,
        lastName: String
    ): Result<Boolean> =
        try {
            auth.createUserWithEmailAndPassword(email, password).await()
            val user = User(firstName, lastName, email)
            saveUserToFirestore(user)
            Result.Success(true)
        } catch (e: Exception) {
            Log.e("SignUpError", "Error signing up", e)
            Result.Error(e)
        }

    private suspend fun saveUserToFirestore(user: User) {
        try {
            val uid = auth.currentUser?.uid ?: throw IllegalStateException("User ID is null")
            firestore.collection("users").document(uid).set(user).await()
        } catch (e: Exception) {
            Log.e("FirestoreError", "Error saving user to Firestore", e)
            throw e
        }
    }

    suspend fun login(email: String, password: String): Result<Boolean> =
        try {
            auth.signInWithEmailAndPassword(email, password).await()
            Result.Success(true)
        } catch (e: Exception) {
            Log.e("LoginError", "Error logging in", e)
            Result.Error(e)
        }

    // New function to retrieve the current user
    suspend fun getCurrentUser(): Result<User> {
        return try {
            val user = auth.currentUser
            if (user != null) {
                // Fetch user data from Firestore using the current user's UID
                val userData = firestore.collection("users").document(user.uid).get().await()
                val currentUser = userData.toObject(User::class.java)
                if (currentUser != null) {
                    Result.Success(currentUser)
                } else {
                    Result.Error(Exception("User not found"))
                }
            } else {
                Result.Error(Exception("No authenticated user"))
            }
        } catch (e: Exception) {
            Log.e("GetUserError", "Error fetching current user", e)
            Result.Error(e)
        }
    }
}
