package com.ams.gardencheck.data.dao

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import com.ams.gardencheck.data.entities.PlantDisease
import kotlinx.coroutines.flow.Flow

@Dao
interface PlantDiseaseDao {
    @Insert
    suspend fun insertPlantDisease(plantDisease: PlantDisease)

    @Update
    suspend fun updatePlantDisease(plantDisease: PlantDisease)

    @Delete
    suspend fun deletePlantDisease(plantDisease: PlantDisease)

    @Query("SELECT * FROM plantDiseases")
    fun getPlantDiseases(): Flow<List<PlantDisease>>
}