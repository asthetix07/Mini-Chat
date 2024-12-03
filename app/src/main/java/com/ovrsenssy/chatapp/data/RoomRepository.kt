package com.ovrsenssy.chatapp.data

import android.util.Log
import com.google.firebase.firestore.FirebaseFirestore
import kotlinx.coroutines.tasks.await

class RoomRepository(private val firestore: FirebaseFirestore) {

    suspend fun createRoom(name: String): Result<Unit> = try {
        val roomRef = firestore.collection("rooms").document() // Generate document reference
        val room = Room(id = roomRef.id, name = name)          // Include the generated ID
        roomRef.set(room).await()                              // Save room with ID
        Result.Success(Unit)
    } catch (e: Exception) {
        Log.e("Creation", "Error creating room", e)
        Result.Error(e)
    }

    suspend fun getRooms(): Result<List<Room>> = try {
        val querySnapshot = firestore.collection("rooms").get().await()
        val rooms = querySnapshot.documents.mapNotNull { document ->
            document.toObject(Room::class.java)?.copy(id = document.id)
        }
        Result.Success(rooms)
    } catch (e: Exception) {
        Log.e("Failed", "Error getting rooms", e)
        Result.Error(e)
    }

}