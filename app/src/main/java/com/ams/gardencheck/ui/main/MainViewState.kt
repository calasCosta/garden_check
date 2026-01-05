package com.ams.gardencheck.ui.main

//import com.ams.gardencheck.Screen
import com.ams.gardencheck.data.entities.PlantDisease
import com.ams.gardencheck.data.entities.User
import com.ams.gardencheck.data.entities.UserPlantDisease

data class MainViewState (
    val plantDiseases: List<PlantDisease> = emptyList(),
    val userPlantDiseases: List<UserPlantDisease> = emptyList(),
    val users: List<User> = emptyList(),
    val alertState: Boolean = false,
    val openDialog: Boolean = false,
    val alertStateU: Boolean = false,
    val openDialogU: Boolean = false,
    val alertStateUP: Boolean = false,
    val openDialogUP: Boolean = false,
    val editPlantDisease: PlantDisease = PlantDisease(diseaseTitle = "", imagePath = "",confidence = "", description = "",createdDate=""),
    val addPlantDisease: PlantDisease = PlantDisease(diseaseTitle = "", imagePath = "",confidence = "", description = "",createdDate=""),
    val editUserPlantDisease: UserPlantDisease = UserPlantDisease(userId = 0, plantDiseaseId = 0),
    val addUserPlantDisease: UserPlantDisease = UserPlantDisease(userId = 0, plantDiseaseId = 0),
    val editUser: User = User(userId = 0, username = "", email = "", createdDate = ""),
    val addUser: User = User(userId = 0, username = "", email = "", createdDate = ""),
    val searchText: String = ""
    //val selectedScreen: Screen = Screen.First
)