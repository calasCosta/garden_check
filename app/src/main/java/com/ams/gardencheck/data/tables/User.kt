package com.ams.gardencheck.data.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "users")
data class User (
    val username: String,
    val email: String,
    val createdDate: String,
    @PrimaryKey(autoGenerate = true)val userId: Int = 0
)