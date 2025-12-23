package com.ams.gardencheck.data.tables

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "plantDiseases")
data class PlantDisease (
    val disease_title: String,
    val image_path: String,
    val confidence: String,
    val description: String,
    val createdDate: String,
    @PrimaryKey(autoGenerate = true)val plantDiseaseId: Int = 0
)
