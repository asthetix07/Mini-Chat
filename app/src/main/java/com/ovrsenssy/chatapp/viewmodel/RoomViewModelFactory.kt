package com.ovrsenssy.chatapp.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.ovrsenssy.chatapp.data.RoomRepository

class RoomViewModelFactory(
    private val roomRepository: RoomRepository
) : ViewModelProvider.Factory {

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(RoomViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return RoomViewModel(roomRepository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}
