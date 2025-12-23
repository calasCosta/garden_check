package com.rjcr.library.data

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ams.gardencheck.data.tables.User

@Database(entities = [User::class], version = 1)
abstract class UserDatabase: RoomDatabase() {
    abstract val dao: UserDao
}