package com.ams.gardencheck.data.interfaces

import androidx.room.Database
import androidx.room.RoomDatabase
import com.ams.gardencheck.data.dao.PlantDiseaseDao
import com.ams.gardencheck.data.entities.PlantDisease

@Database(entities = [PlantDisease::class], version = 1)
abstract class PlantDiseaseDatabase: RoomDatabase() {
    abstract val dao: PlantDiseaseDao
}