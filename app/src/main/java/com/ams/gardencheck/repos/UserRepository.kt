package com.ams.gardencheck.repos

// UserRepository.kt
import com.ams.gardencheck.data.dao.UserDao
import com.ams.gardencheck.data.entities.User
import kotlinx.coroutines.flow.Flow

class UserRepository(private val userDao: UserDao) {

    fun getUsers(): Flow<List<User>> = userDao.getUsers()

    suspend fun getUserByEmail(email: String): User? = userDao.getUserByEmail(email)

    suspend fun getUserByGoogleId(googleId: String): User? = userDao.getUserByGoogleId(googleId)

    suspend fun insertUser(user: User) = userDao.insertUser(user)

    suspend fun updateUser(user: User) = userDao.updateUser(user)

    suspend fun deleteUser(user: User) = userDao.deleteUser(user)

    suspend fun getCurrentUser(): User? = userDao.getLoggedInUser()

    suspend fun logoutAllUsers() = userDao.logoutAllUsers()
}