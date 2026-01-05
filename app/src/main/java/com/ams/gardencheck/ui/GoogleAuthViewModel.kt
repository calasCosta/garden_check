package com.ams.gardencheck.ui

// GoogleAuthViewModel.kt
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.ams.gardencheck.data.entities.User
import com.ams.gardencheck.repos.UserRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.launch

class GoogleAuthViewModel(
    private val userRepository: UserRepository
) : ViewModel() {

    private val _authState = MutableStateFlow<AuthState>(AuthState.Idle)
    val authState: StateFlow<AuthState> = _authState.asStateFlow()

    private val _currentUser = MutableStateFlow<User?>(null)
    val currentUser: StateFlow<User?> = _currentUser.asStateFlow()

    fun saveUserFromFirebase(
        uid: String,
        email: String,
        name: String,
        photoUrl: String?
    ) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading

                val currentTime = System.currentTimeMillis().toString()

                // Check if user exists by email or googleId
                val existingUser = userRepository.getUserByEmail(email)
                    ?: userRepository.getUserByGoogleId(uid)

                if (existingUser != null) {
                    // Update existing user
                    val updatedUser = existingUser.copy(
                        googleId = uid,
                        username = name.takeIf { it.isNotBlank() } ?: existingUser.username,
                        lastLogin = currentTime,
                        isLoggedIn = true,
                        profilePicture = photoUrl ?: existingUser.profilePicture
                    )
                    userRepository.updateUser(updatedUser)
                    _authState.value = AuthState.Success(updatedUser)
                } else {
                    // Create new user with Firebase UID
                    val newUser = User(
                        username = name.ifEmpty { email.substringBefore("@") },
                        email = email,
                        googleId = uid,
                        profilePicture = photoUrl,
                        registeredAt = currentTime,
                        lastLogin = currentTime,
                        isLoggedIn = true
                    )
                    userRepository.insertUser(newUser)

                    _currentUser.value = newUser
                    _authState.value = AuthState.Success(newUser)
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error(e.message ?: "Failed to save user")
            }
        }
    }

    fun checkAndUpdateUser(
        uid: String,
        email: String,
        name: String,
        photoUrl: String?
    ) {
        viewModelScope.launch {
            try {
                _authState.value = AuthState.Loading

                val existingUser = userRepository.getUserByGoogleId(uid)
                    ?: userRepository.getUserByEmail(email)

                if (existingUser != null) {
                    // User exists, just update login status
                    val updatedUser = existingUser.copy(
                        lastLogin = System.currentTimeMillis().toString(),
                        isLoggedIn = true
                    )
                    userRepository.updateUser(updatedUser)
                    _currentUser.value = updatedUser
                    _authState.value = AuthState.Success(updatedUser)
                } else {
                    // User doesn't exist in local DB, create new
                    saveUserFromFirebase(uid, email, name, photoUrl)
                }
            } catch (e: Exception) {
                _authState.value = AuthState.Error("Session restore failed")
            }
        }
    }

    // Load current user from database
    fun loadCurrentUser() {
        viewModelScope.launch {
            try {
                val user = userRepository.getCurrentUser()
                _currentUser.value = user
                if (user != null) {
                    _authState.value = AuthState.Success(user)
                }
            } catch (e: Exception) {
                // No user logged in, that's ok
                _currentUser.value = null
            }
        }
    }

    // Get current user synchronously (for ProfileScreen)
    fun getCurrentUser(): User? = _currentUser.value

    // Clear current user (on logout)
    private fun clearCurrentUser() {
        _currentUser.value = null
    }

    fun handleError(message: String) {
        _authState.value = AuthState.Error(message)
    }

    fun logout() {
        viewModelScope.launch {
            try {
                userRepository.logoutAllUsers()
                clearCurrentUser()
                _authState.value = AuthState.Idle
            } catch (e: Exception) {
                clearCurrentUser()
                _authState.value = AuthState.Idle
            }
        }
    }

    fun deleteAccount(user: User){
        viewModelScope.launch {
            userRepository.deleteUser(user)
        }
    }

    sealed class AuthState {
        object Idle : AuthState()
        object Loading : AuthState()
        data class Success(val user: User) : AuthState()
        data class Error(val message: String) : AuthState()
    }
}

// ViewModel Factory
class GoogleAuthViewModelFactory(
    private val repository: UserRepository
) : androidx.lifecycle.ViewModelProvider.Factory {
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        if (modelClass.isAssignableFrom(GoogleAuthViewModel::class.java)) {
            @Suppress("UNCHECKED_CAST")
            return GoogleAuthViewModel(repository) as T
        }
        throw IllegalArgumentException("Unknown ViewModel class")
    }
}