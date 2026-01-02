package com.ams.gardencheck.data.entities

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plantDiseases")
data class PlantDisease (
    val diseaseTitle: String,
    val imagePath: String,
    val confidence: String,
    val description: String,
    val createdDate: String,
    @PrimaryKey(autoGenerate = true)val plantDiseaseId: Int = 0
)