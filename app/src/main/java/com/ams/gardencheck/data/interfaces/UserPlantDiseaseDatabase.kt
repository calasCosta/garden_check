package com.ams.gardencheck.data.interfaces

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ams.gardencheck.data.dao.UserPlantDiseaseDao
import com.ams.gardencheck.data.entities.UserPlantDisease

@Database(entities = [UserPlantDisease::class], version = 1)
abstract class UserPlantDiseaseDatabase: RoomDatabase() {
    abstract val dao: UserPlantDiseaseDao
}