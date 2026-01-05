package com.ams.gardencheck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.contract.ActivityResultContracts
import androidx.activity.viewModels
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.padding
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import androidx.room.Room
import com.ams.gardencheck.components.MainBottomNavigation
import com.ams.gardencheck.data.interfaces.PlantDiseaseDatabase
import com.ams.gardencheck.data.interfaces.UserDatabase
import com.ams.gardencheck.data.interfaces.UserPlantDiseaseDatabase
import com.ams.gardencheck.ui.main.MainViewModel
import com.ams.gardencheck.ui.screens.HomeScreen
import com.ams.gardencheck.ui.screens.ImageCaptureScreen
import com.ams.gardencheck.ui.screens.LoginScreen
import com.ams.gardencheck.ui.screens.ProfileScreen
import com.ams.gardencheck.ui.screens.TermsAndPrivacyScreen
import com.ams.gardencheck.ui.theme.GardencheckTheme

class MainActivity : ComponentActivity() {

    private val db by lazy {
        Room.databaseBuilder(this, PlantDiseaseDatabase::class.java, "PlantDiseaseDatabase.db").build()
    }

    private val dbU by lazy {
        Room.databaseBuilder(this, UserDatabase::class.java, "UserDatabase.db").build()
    }

    private val dbUP by lazy {
        Room.databaseBuilder(this, UserPlantDiseaseDatabase::class.java, "UserPlantDiseaseDatabase.db").build()
    }

    private val vm by viewModels<MainViewModel>(
        factoryProducer = {
            object : ViewModelProvider.Factory {
                override fun <T : ViewModel> create(modelClass: Class<T>): T {
                    return MainViewModel(db.dao, dbU.dao, dbUP.dao) as T
                }
            }
        }
    )


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GardencheckTheme {
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                // Check if we're on a main app screen
                val currentRoute = navController
                    .currentBackStackEntryAsState()
                    .value
                    ?.destination
                    ?.route

                val isMainAppScreen = currentRoute in listOf("First", "Second", "Third")

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {
                        if (isMainAppScreen) {
                            MainBottomNavigation(navController)
                        }
                    },
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "Login",
                        modifier = Modifier
                            .padding(innerPadding)
                            .fillMaxSize()
                    ) {
                        // Login Screen
                        composable("Login") {
                            LoginScreen(
                                onLoginSuccess = {
                                    // Navigate to MainApp when login succeeds
                                    navController.navigate("First") {
                                        // Clear back stack so user can't go back to login
                                        popUpTo("Login") { inclusive = true }
                                    }
                                },
                                onTermsClicked = {
                                    // Navigate to Terms and Privacy screen
                                    navController.navigate("TermsAndPrivacy")
                                }
                            )
                        }

                        // Terms and Privacy Screen
                        composable("TermsAndPrivacy") {
                            TermsAndPrivacyScreen(
                                onBackClicked = {
                                    navController.navigateUp()
                                }
                            )
                        }


                        composable("First") {
                            HomeScreen(Modifier, vm,navController)
                        }
                        composable("Second") {
                            ImageCaptureScreen(Modifier, vm,navController)
                        }
                        composable("Third") {
                            ProfileScreen(
                                onLogoutClicked = {
                                    // ToDo: Logout
                                    navController.navigate("Login")
                                }
                            )
                        }
                    }
                }
            }
        }
    }
}