package com.ams.gardencheck.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "userPlantDiseases")
data class UserPlantDisease (
    val userId : Int,
    @PrimaryKey val plantDiseaseId: Int
)