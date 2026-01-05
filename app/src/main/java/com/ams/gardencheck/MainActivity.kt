package com.ams.gardencheck

import android.app.AlertDialog
import android.app.ProgressDialog
import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.enableEdgeToEdge
import androidx.activity.result.ActivityResultLauncher
import androidx.activity.result.contract.ActivityResultContracts
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.Scaffold
import androidx.compose.material3.SnackbarHost
import androidx.compose.material3.SnackbarHostState
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Modifier
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.navigation.NavHostController
import androidx.navigation.compose.NavHost
import androidx.navigation.compose.composable
import androidx.navigation.compose.currentBackStackEntryAsState
import androidx.navigation.compose.rememberNavController
import com.ams.gardencheck.components.MainBottomNavigation
import com.ams.gardencheck.data.interfaces.UserDatabase
import com.ams.gardencheck.repos.UserRepository
import com.ams.gardencheck.ui.GoogleAuthViewModel
import com.ams.gardencheck.ui.screens.HomeScreen
import com.ams.gardencheck.ui.screens.ImageCaptureScreen
import com.ams.gardencheck.ui.screens.LoginScreen
import com.ams.gardencheck.ui.screens.ProfileScreen
import com.ams.gardencheck.ui.screens.TermsAndPrivacyScreen
import com.ams.gardencheck.ui.theme.GardencheckTheme
import com.google.android.gms.auth.api.signin.GoogleSignIn
import com.google.android.gms.auth.api.signin.GoogleSignInClient
import com.google.android.gms.auth.api.signin.GoogleSignInOptions
import com.google.android.gms.common.api.ApiException
import com.google.firebase.auth.FirebaseAuth
import com.google.firebase.auth.GoogleAuthProvider

class MainActivity : ComponentActivity() {
    private lateinit var googleSignInClient: GoogleSignInClient
    private lateinit var auth: FirebaseAuth
    private lateinit var googleSignInLauncher: ActivityResultLauncher<Intent>
    private lateinit var viewModel: GoogleAuthViewModel  // Declare at class level

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        enableEdgeToEdge()

        // Initialize Firebase Auth
        auth = FirebaseAuth.getInstance()

        // Initialize ViewModel HERE (before setContent)
        val database = UserDatabase.getDatabase(this)
        val repository = UserRepository(database.dao)
        viewModel = GoogleAuthViewModel(repository)  // Create instance once

        // Configure Google Sign-In
        val gso = GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
            .requestIdToken(getString(R.string.default_web_client_id))
            .requestEmail()
            .build()

        googleSignInClient = GoogleSignIn.getClient(this, gso)

        // Setup Google Sign-In launcher
        googleSignInLauncher = registerForActivityResult(
            ActivityResultContracts.StartActivityForResult()
        ) { result ->
            val task = GoogleSignIn.getSignedInAccountFromIntent(result.data)
            try {
                val account = task.getResult(ApiException::class.java)
                account?.idToken?.let { idToken ->
                    firebaseAuthWithGoogle(idToken)
                }
            } catch (e: ApiException) {
                // Handle error using the existing viewModel
                viewModel.handleError("Google sign in failed: ${e.statusCode}")
            }
        }

        setContent {
            GardencheckTheme {
                val navController = rememberNavController()
                val snackbarHostState = remember { SnackbarHostState() }

                // Collect authentication state from the SAME ViewModel instance
                val authState by viewModel.authState.collectAsStateWithLifecycle()

                // Handle authentication state changes
                LaunchedEffect(authState) {
                    when (authState) {
                        is GoogleAuthViewModel.AuthState.Success -> {
                            // Navigate to main app on successful login
                            navController.navigate("First") {
                                popUpTo("Login") { inclusive = true }
                            }
                        }
                        is GoogleAuthViewModel.AuthState.Error -> {
                            // Show error message
                            val errorMessage = (authState as GoogleAuthViewModel.AuthState.Error).message
                            snackbarHostState.showSnackbar(errorMessage)
                        }
                        else -> {
                            // Loading or Idle state - do nothing
                        }
                    }
                }

                // Check if user is already logged in (on app start)
                LaunchedEffect(Unit) {
                    val currentFirebaseUser = auth.currentUser
                    if (currentFirebaseUser != null) {
                        // Use the same viewModel instance
                        viewModel.checkAndUpdateUser(
                            uid = currentFirebaseUser.uid,
                            email = currentFirebaseUser.email ?: "",
                            name = currentFirebaseUser.displayName ?: "",
                            photoUrl = currentFirebaseUser.photoUrl?.toString()
                        )
                    }
                }

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
                            var isLoading by remember { mutableStateOf(false) }

                            LaunchedEffect(authState) {
                                // Update loading state based on auth state
                                isLoading = authState is GoogleAuthViewModel.AuthState.Loading
                            }

                            LoginScreen(
                                isLoading = isLoading,
                                onGoogleSignInClicked = {
                                    signInWithGoogle()
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
                                viewModel = viewModel,
                                onLogoutClicked = {
                                    // Sign out from Firebase, Google, and local database
                                    signOut(navController)
                                },
                                onDeleteAccountClicked = {
                                    deleteAccount(navController)
                                }
                            )
                        }
                    }
                }
            }
        }
    }

    private fun signInWithGoogle() {
        val signInIntent = googleSignInClient.signInIntent
        googleSignInLauncher.launch(signInIntent)
    }

    private fun firebaseAuthWithGoogle(idToken: String) {
        val credential = GoogleAuthProvider.getCredential(idToken, null)
        auth.signInWithCredential(credential)
            .addOnCompleteListener(this) { task ->
                if (task.isSuccessful) {
                    // Firebase authentication successful
                    val firebaseUser = auth.currentUser
                    firebaseUser?.let { user ->
                        // Use the SAME viewModel instance (not create a new one!)
                        viewModel.saveUserFromFirebase(
                            uid = user.uid,
                            email = user.email ?: "",
                            name = user.displayName ?: user.email?.substringBefore("@") ?: "User",
                            photoUrl = user.photoUrl?.toString()
                        )
                    }
                } else {
                    // Handle Firebase authentication error using the same viewModel
                    viewModel.handleError("Firebase auth failed: ${task.exception?.message}")
                }
            }
    }

    private fun signOut(navController: NavHostController) {
        // 1. Sign out from Firebase
        auth.signOut()

        // 2. Sign out from Google
        googleSignInClient.signOut().addOnCompleteListener {
            // 3. Clear local database session using the same viewModel
            viewModel.logout()

            // 4. Navigate to login screen
            navController.navigate("Login") {
                popUpTo(0) // Clear entire back stack
            }
        }
    }

    private fun deleteAccount(navController: NavHostController) {
        AlertDialog.Builder(this)
            .setTitle("Delete Account")
            .setMessage("Delete your local account data? You can still sign in again with Google.")
            .setPositiveButton("Delete") { dialog, which ->
                val progressDialog = ProgressDialog(this).apply {
                    setMessage("Deleting account...")
                    setCancelable(false)
                    show()
                }
                val currentLocalUser = viewModel.getCurrentUser()
                if(currentLocalUser != null)
                    // Only delete from local database
                    viewModel.deleteAccount(currentLocalUser)

                // Sign out
                auth.signOut()
                googleSignInClient.signOut()

                progressDialog.dismiss()

                // Navigate to login
                navController.navigate("Login") {
                    popUpTo(0)
                }

                Toast.makeText(
                    this,
                    "Account data deleted",
                    Toast.LENGTH_LONG
                ).show()
            }
            .setNegativeButton("Cancel", null)
            .show()
    }
}