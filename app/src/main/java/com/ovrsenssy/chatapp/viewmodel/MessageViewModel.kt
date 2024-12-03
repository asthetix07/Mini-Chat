package com.ovrsenssy.chatapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovrsenssy.chatapp.data.Message
import com.ovrsenssy.chatapp.data.MessageRepository
import com.ovrsenssy.chatapp.data.Result
import com.ovrsenssy.chatapp.data.User
import com.ovrsenssy.chatapp.data.UserRepository
import kotlinx.coroutines.launch


class MessageViewModel(
    private val messageRepository: MessageRepository,
    private val userRepository: UserRepository
) : ViewModel() {

    private val _messages = MutableLiveData<List<Message>>()
    val messages: LiveData<List<Message>> get() = _messages

    private val _roomId = MutableLiveData<String>()
    private val _currentUser = MutableLiveData<User>()
    val currentUser: LiveData<User> get() = _currentUser

    init {
        loadCurrentUser()
    }

    private fun loadCurrentUser() {
        viewModelScope.launch {
            when (val result = userRepository.getCurrentUser()) {
                is Result.Success -> _currentUser.value = result.data
                is Result.Error -> {
                    // Handle error (maybe show a snackbar or log it)
                }
            }
        }
    }

    fun loadMessages() {
        viewModelScope.launch {
            _roomId.value?.let { roomId ->
                messageRepository.getChatMessages(roomId)
                    .collect { _messages.value = it }
            }
        }
    }

    fun setRoomId(roomId: String) {
        _roomId.value = roomId
        loadMessages()
    }

    fun sendMessage(text: String) {
        _currentUser.value?.let { currentUser ->
            val message = Message(
                senderFirstName = currentUser.firstName,
                senderId = currentUser.email,
                text = text
            )
            viewModelScope.launch {
                when (val result = messageRepository.sendMessage(_roomId.value ?: "", message)) {
                    is Result.Success -> Unit
                    is Result.Error -> { /* Handle error */ }
                }
            }
        }
    }
}
