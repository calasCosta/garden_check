package com.ams.gardencheck.ui.main

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
//import com.ams.gardencheck.Screen
import com.ams.gardencheck.data.tables.PlantDisease
import com.ams.gardencheck.data.tables.PlantDiseaseDao
import com.ams.gardencheck.data.tables.User
import com.ams.gardencheck.data.tables.UserPlantDisease
import com.ams.gardencheck.data.tables.UserPlantDiseaseDao
import com.example.books.ui.MainViewState
import com.rjcr.library.data.UserDao
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.flow.asStateFlow
import kotlinx.coroutines.flow.update
import kotlinx.coroutines.launch

class MainViewModel(private val dao: PlantDiseaseDao, private val daoU: UserDao, private val daoUP: UserPlantDiseaseDao): ViewModel() {
    private val _mainViewState = MutableStateFlow(MainViewState())
    val mainViewState: StateFlow<MainViewState> = _mainViewState.asStateFlow()

    // começo de PlantDisease
    fun savePlantDisease(plantDisease: PlantDisease){
        viewModelScope.launch {
            dao.insertPlantDisease(plantDisease)
            toggleAlert()
            getPlantDiseases()
            closeDialog()
        }
    }

    fun getPlantDiseases(){
        viewModelScope.launch {
            dao.getPlantDiseases().collect{ data ->
                _mainViewState.update { it.copy(plantDiseases = data) }
            }
        }
    }

    fun deletePlantDisease(plantDisease: PlantDisease): Boolean{
        viewModelScope.launch {
            dao.deletePlantDisease(plantDisease)
            getPlantDiseases()
        }
        return true
    }

    fun toggleAlert(){
        _mainViewState.update { it.copy(alertState = !it.alertState) }
    }

    fun closeDialog(){
        _mainViewState.update { it.copy(openDialog = false) }
    }

    fun editPlantDisease(plantDisease: PlantDisease){
        _mainViewState.update { it.copy(openDialog = true, editPlantDisease = plantDisease) }
    }

    fun updatePlantDisease(plantDisease: PlantDisease){
        viewModelScope.launch {
            dao.updatePlantDisease(plantDisease)
            getPlantDiseases()
            closeDialog()
        }
    }

    /*fun selectScreen(screen: Screen){
        _mainViewState.update { it.copy(selectedScreen = screen) }
    }*/

    // fim de PlantDisease

    // começo de UserPlantDisease

    fun saveUserPlantDisease(userPlantDisease: UserPlantDisease){
        viewModelScope.launch {
            daoUP.insertUserPlantDisease(userPlantDisease)
            toggleAlertUP()
            getUserPlantDiseases()
            closeDialogUP()
        }
    }

    fun getUserPlantDiseases(){
        viewModelScope.launch {
            daoUP.getUserPlantDiseases().collect{ data ->
                _mainViewState.update { it.copy(userPlantDiseases = data) }
            }
        }
    }

    fun deleteUserPlantDisease(userPlantDisease: UserPlantDisease): Boolean{
        viewModelScope.launch {
            daoUP.deleteUserPlantDisease(userPlantDisease)
            getPlantDiseases()
        }
        return true
    }

    fun toggleAlertUP(){
        _mainViewState.update { it.copy(alertState = !it.alertStateUP) }
    }

    fun closeDialogUP(){
        _mainViewState.update { it.copy(openDialog = false) }
    }

    fun editUserPlantDisease(userPlantDisease: UserPlantDisease){
        _mainViewState.update { it.copy(openDialog = true, editUserPlantDisease = userPlantDisease) }
    }

    fun updateUserPlantDisease(userPlantDisease: UserPlantDisease){
        viewModelScope.launch {
            daoUP.updateUserPlantDisease(userPlantDisease)
            getUserPlantDiseases()
            closeDialogUP()
        }
    }

    /*fun selectScreen(screen: Screen){
        _mainViewState.update { it.copy(selectedScreen = screen) }
    }*/

    // fim de UserPlantDisease

    //começo de User

    fun saveUser(user: User){
        viewModelScope.launch {
            daoU.insertUser(user)
            toggleAlertU()
            getUsers()
            closeDialogU()
        }
    }

    fun getUsers(){
        viewModelScope.launch {
            daoU.getUsers().collect{ data ->
                _mainViewState.update { it.copy(users = data) }
            }
        }
    }

    fun deleteUser(user: User): Boolean{
        viewModelScope.launch {
            daoU.deleteUser(user)
            getUsers()
        }
        return true
    }

    fun toggleAlertU(){
        _mainViewState.update { it.copy(alertState = !it.alertStateU) }
    }

    fun closeDialogU(){
        _mainViewState.update { it.copy(openDialog = false) }
    }

    fun editUser(user: User){
        _mainViewState.update { it.copy(openDialog = true, editUser = user) }
    }

    fun updateUser(user: User){
        viewModelScope.launch {
            daoU.updateUser(user)
            getUsers()
            closeDialogU()
        }
    }

    /*fun selectScreen(screen: Screen){
        _mainViewState.update { it.copy(selectedScreen = screen) }
    }*/

    // Dentro do ViewModel
    fun onSearchTextChanged(newText: String) {
        _mainViewState.value = _mainViewState.value.copy(searchText = newText)
        // Aqui você pode disparar a lógica de filtro da lista
    }
}