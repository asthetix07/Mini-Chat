package com.ovrsenssy.chatapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovrsenssy.chatapp.data.Result.Success
import com.ovrsenssy.chatapp.data.Result.Error
import com.ovrsenssy.chatapp.data.Room
import com.ovrsenssy.chatapp.data.RoomRepository
import kotlinx.coroutines.launch

class RoomViewModel(private val roomRepository: RoomRepository) : ViewModel() {

    private val _rooms = MutableLiveData<List<Room>>()
    val rooms: LiveData<List<Room>> get() = _rooms

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    init {
        loadRooms()
    }

    // Create room function
    fun createRoom(name: String) {
        viewModelScope.launch {
            _isLoading.value = true
            _errorMessage.value = null
            when (val result = roomRepository.createRoom(name)) {
                is Success -> loadRooms()  // Reload rooms after creation
                is Error -> _errorMessage.value = result.exception.message ?: "An unknown error occurred"
            }
            _isLoading.value = false
        }
    }

    // Load rooms function
    fun loadRooms() {
        viewModelScope.launch {
            _isLoading.value = true
            when (val result = roomRepository.getRooms()) {
                is Success -> _rooms.value = result.data  // Set the rooms data
                is Error -> _errorMessage.value = result.exception.message ?: "An unknown error occurred"
            }
            _isLoading.value = false
        }
    }
}