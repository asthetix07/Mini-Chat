package com.ovrsenssy.chatapp.viewmodel

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ovrsenssy.chatapp.data.Result
import com.ovrsenssy.chatapp.data.UserRepository
import kotlinx.coroutines.launch

class AuthViewModel(private val userRepository: UserRepository) : ViewModel() {

    private val _authResult = MutableLiveData<Result<Boolean>?>()
    val authResult: LiveData<Result<Boolean>?> get() = _authResult

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    // SignUp Function
    fun signUp(email: String, password: String, firstName: String, lastName: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _authResult.value = userRepository.signUp(email, password, firstName, lastName)
            } catch (e: Exception) {
                _authResult.value = Result.Error(Throwable(e.message ?: "An error occurred."))
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Login Function
    fun login(email: String, password: String) {
        viewModelScope.launch {
            _isLoading.value = true
            try {
                _authResult.value = userRepository.login(email, password)
            } catch (e: Exception) {
                _authResult.value = Result.Error(Throwable(e.message ?: "An error occurred."))
            } finally {
                _isLoading.value = false
            }
        }
    }

    // Clear auth result
    fun clearAuthResult() {
        _authResult.value = null
    }
}
