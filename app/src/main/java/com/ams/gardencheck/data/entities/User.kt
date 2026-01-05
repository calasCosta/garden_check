package com.ams.gardencheck.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    @PrimaryKey(autoGenerate = true)val userId: Int = 0,
    val username: String,
    val email: String,
    val googleId: String, // For Google authentication
    val profilePicture: String? = null, // Optional
    val registeredAt: String,
    val lastLogin: String,
    val isLoggedIn: Boolean = false // track current session
)