package com.ams.gardencheck

import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
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
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ams.gardencheck.components.MainBottomNavigation
import com.ams.gardencheck.ui.screens.HomeScreen
import com.ams.gardencheck.ui.screens.ImageCaptureScreen
import com.ams.gardencheck.ui.screens.LoginScreen
import com.ams.gardencheck.ui.screens.ProfileScreen
import com.ams.gardencheck.ui.screens.TermsAndPrivacyScreen
import com.ams.gardencheck.ui.theme.GardencheckTheme

class MainActivity : ComponentActivity() {
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
                        modifier = Modifier.fillMaxSize()
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
                            HomeScreen()
                        }
                        composable("Second") {
                            ImageCaptureScreen()
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