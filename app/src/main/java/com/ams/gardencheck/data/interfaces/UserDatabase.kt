package com.ams.gardencheck.data.interfaces

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ams.gardencheck.data.dao.UserDao
import com.ams.gardencheck.data.entities.User

@Database(entities = [User::class], version = 1)
abstract class UserDatabase: RoomDatabase() {
    abstract val dao: UserDao
}