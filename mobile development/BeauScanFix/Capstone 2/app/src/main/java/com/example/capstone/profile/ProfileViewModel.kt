package com.example.capstone.profile

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.capstone.Repository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class ProfileViewModel(private val repository: Repository) : ViewModel() {

    private val _userProfile = MutableStateFlow<User?>(null)
    val userProfile: StateFlow<User?> get() = _userProfile

    private val _loadingState = MutableStateFlow(false)
    val loadingState: StateFlow<Boolean> get() = _loadingState

    fun loadUserProfile(token: String) {
        viewModelScope.launch {
            _loadingState.value = true
            val user = repository.fetchUserProfile(token)
            _userProfile.value = user
            _loadingState.value = false
        }
    }
}