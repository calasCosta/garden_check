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
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.ui.Modifier
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.rememberNavController
import com.ams.gardencheck.components.MainBottomNavigation
import com.ams.gardencheck.ui.screens.HomeScreen
import com.ams.gardencheck.ui.screens.ImageCaptureScreen
import com.ams.gardencheck.ui.screens.ProfileScreen
import com.ams.gardencheck.ui.theme.GardencheckTheme

class MainActivity : ComponentActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()
        setContent {
            GardencheckTheme {
                val navController = rememberNavController()

                //snackbar
                val snackbarHostState = remember { SnackbarHostState() }
                val scope = rememberCoroutineScope()

                Scaffold(
                    modifier = Modifier.fillMaxSize(),
                    bottomBar = {MainBottomNavigation(navController)},
                    snackbarHost = { SnackbarHost(snackbarHostState) }
                ) { innerPadding ->
                    NavHost(
                        navController = navController,
                        startDestination = "First",
                        modifier = Modifier.padding(innerPadding)
                    ){
                        composable("First") {
                            HomeScreen()
                        }
                        composable("Second") {
                            ImageCaptureScreen()
                        }
                        composable ("Third"){
                            ProfileScreen()
                        }
                    }
                }
            }
        }
    }
}