package com.ams.gardencheck.ui.screens

import androidx.compose.foundation.Image
import androidx.compose.foundation.background
import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Box
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.layout.wrapContentWidth
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Person
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Card
import androidx.compose.material3.CardDefaults
import androidx.compose.material3.Divider
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Scaffold
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.LaunchedEffect
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.draw.clip
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.layout.ContentScale
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.lifecycle.compose.collectAsStateWithLifecycle
import androidx.lifecycle.viewmodel.compose.viewModel
import coil.compose.rememberAsyncImagePainter
import com.ams.gardencheck.R
import com.ams.gardencheck.data.entities.User
import com.ams.gardencheck.ui.GoogleAuthViewModel
import java.text.SimpleDateFormat
import java.util.Date
import java.util.Locale

@Composable
fun ProfileScreen(
    modifier: Modifier = Modifier,
    viewModel: GoogleAuthViewModel,
    onLogoutClicked: ()-> Unit,
    onDeleteAccountClicked: ()-> Unit
){

    // State for user data
    val currentUser by viewModel.currentUser.collectAsStateWithLifecycle()
    var isLoading by remember { mutableStateOf(true) }

    // Load current user when screen appears
    LaunchedEffect(Unit) {
        viewModel.loadCurrentUser()
        isLoading = false
    }



        Column(
            modifier = Modifier
                .fillMaxSize()
                .padding(16.dp)
        ) {
            Spacer(modifier = Modifier.height(100.dp))

            // Title
            Text(
                text = "Profile",
                fontSize = 24.sp,
                fontWeight = FontWeight.Bold
            )

            Spacer(modifier = Modifier.height(8.dp))

            // Profile Picture
            if (currentUser?.profilePicture != null) {
                Image(
                    painter = rememberAsyncImagePainter(currentUser!!.profilePicture),
                    contentDescription = "Profile Picture",
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape),
                    contentScale = ContentScale.Crop
                )
            } else {
                Box(
                    modifier = Modifier
                        .size(70.dp)
                        .clip(CircleShape)
                        .background(Color(0xFFE0E0E0)),
                    contentAlignment = Alignment.Center
                ) {
                    Icon(
                        imageVector = Icons.Default.Person,
                        contentDescription = "Profile Placeholder",
                        modifier = Modifier.size(70.dp),
                        tint = Color(0xFF757575)
                    )
                }
            }

            Spacer(modifier = Modifier.height(24.dp))

            // Profile info
            ProfileInfoRow("Username:", currentUser?.username ?: "—")
            Spacer(modifier = Modifier.height(12.dp))

            ProfileInfoRow("Email:", currentUser?.email ?: "—")
            Spacer(modifier = Modifier.height(12.dp))

            ProfileInfoRow(
                "Registered at:",
                formatDate(currentUser?.registeredAt) ?: "—"
            )

            Spacer(modifier = Modifier.height(24.dp))
            Divider(modifier = Modifier.width(300.dp))
            Spacer(modifier = Modifier.height(24.dp))

            // Sign out button
            Button(
                onClick = onLogoutClicked,
                modifier = Modifier
                    .wrapContentWidth()
                    .height(44.dp),
                colors = ButtonDefaults.buttonColors(
                    containerColor = Color(0xFFE0E0E0)
                )
            ) {
                Text(
                    text = "Sign out",
                    color = Color.Black
                )
            }

            Spacer(modifier = Modifier.height(16.dp))

            // Delete account (text button)
            Text(
                text = "Delete Account",
                color = Color.Red,
                modifier = Modifier
                    .clickable(onClick = onDeleteAccountClicked)
                    .padding(vertical = 8.dp)
            )
        }



}
// Helper function to format timestamp
private fun formatDate(timestamp: String?): String? {
    return try {
        timestamp?.toLongOrNull()?.let {
            val date = Date(it)
            val formatter = SimpleDateFormat("dd/MM/yyyy HH:mm:ss", Locale.getDefault())
            formatter.format(date)
        }
    } catch (e: Exception) {
        null
    }
}

@Composable
fun ProfileInfoRow(label: String, value: String) {
    Row {
        Text(
            text = label,
            fontWeight = FontWeight.Medium
        )
        Spacer(modifier = Modifier.width(6.dp))
        Text(text = value)
    }
}