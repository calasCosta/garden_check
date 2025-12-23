package com.ams.gardencheck.data.tables

import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.Query
import androidx.room.Update
import kotlinx.coroutines.flow.Flow

@Dao
interface UserPlantDiseaseDao {
    @Insert
    suspend fun insertUserPlantDisease(userPlantDisease: UserPlantDisease)

    @Update
    suspend fun updateUserPlantDisease(userPlantDisease: UserPlantDisease)

    @Delete
    suspend fun deleteUserPlantDisease(userPlantDisease: UserPlantDisease)

    @Query("SELECT * FROM userPlantDiseases")
    fun getUserPlantDiseases(): Flow<List<UserPlantDisease>>
}