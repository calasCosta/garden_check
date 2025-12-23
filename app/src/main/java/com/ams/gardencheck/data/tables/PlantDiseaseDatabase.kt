package com.ams.gardencheck.data.tables

import androidx.room.Database
import androidx.room.RoomDatabase
import com.rjcr.library.data.UserDao

@Database(entities = [PlantDisease::class], version = 1)
abstract class PlantDiseaseDatabase: RoomDatabase() {
    abstract val dao: PlantDiseaseDao
}