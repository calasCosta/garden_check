package com.ams.gardencheck.data.tables

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = [UserPlantDisease::class], version = 1)
abstract class UserPlantDiseaseDatabase: RoomDatabase() {
    abstract val dao: UserPlantDiseaseDao
}