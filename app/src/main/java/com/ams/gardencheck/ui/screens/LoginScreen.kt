package com.ams.gardencheck.ui.screens

import androidx.compose.foundation.BorderStroke
import androidx.compose.foundation.layout.Arrangement
import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.Row
import androidx.compose.foundation.layout.Spacer
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.foundation.layout.height
import androidx.compose.foundation.layout.padding
import androidx.compose.foundation.layout.size
import androidx.compose.foundation.layout.width
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material3.Button
import androidx.compose.material3.ButtonDefaults
import androidx.compose.material3.Icon
import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.runtime.getValue
import androidx.compose.runtime.mutableStateOf
import androidx.compose.runtime.remember
import androidx.compose.runtime.rememberCoroutineScope
import androidx.compose.runtime.setValue
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import com.ams.gardencheck.R
import com.ams.gardencheck.components.ClickableTermsText
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

@Composable
fun LoginScreen(
    modifier: Modifier = Modifier,
    onLoginSuccess: () -> Unit,
    onTermsClicked: () -> Unit
){
    var isLoading by remember { mutableStateOf<Boolean>(false) }
    val scope = rememberCoroutineScope()

    Column(
        modifier = modifier
            .fillMaxSize()
            .padding(horizontal = 32.dp),
        horizontalAlignment = Alignment.CenterHorizontally,
        verticalArrangement = Arrangement.Center
    ) {

        Icon(
            painter = painterResource(id = R.drawable.ic_logo),
            contentDescription = "Google",
            modifier = Modifier.size(80.dp),
            tint = Color.Unspecified
        )
        // Title
        Text(
            text = "GardenCheck",
            fontSize = 16.sp,
            fontWeight = FontWeight.Normal,
            color = Color.Black,
            modifier = Modifier.padding(bottom = 200.dp)
        )

        // "Continue with Google" button
        Button(
            onClick = {
                isLoading = true
                // Use the coroutine scope to launch the async operation
                scope.launch {
                    delay(1500) // Simulate network delay
                    isLoading = false
                    onLoginSuccess() // Call this AFTER successful login
                }
            },
            enabled = !isLoading,

            modifier = Modifier
                .fillMaxWidth()
                .height(56.dp),
            colors = ButtonDefaults.buttonColors(
                containerColor = Color.White,
                contentColor = Color.Black
            ),
            shape = RoundedCornerShape(20.dp),
            elevation = ButtonDefaults.buttonElevation(
                defaultElevation = 0.dp,
                pressedElevation = 1.dp,
                disabledElevation = 0.dp
            ),

            border = BorderStroke(1.dp, Color(0xFFEEEEEE))
        ) {
            Row(
                verticalAlignment = Alignment.CenterVertically,
                horizontalArrangement = Arrangement.Center,
                modifier = Modifier.fillMaxWidth()
            ) {
                // Google icon
                Icon(
                    painter = painterResource(id = R.drawable.icons8_google_48),
                    contentDescription = "Google",
                    modifier = Modifier.size(24.dp),
                    tint = Color.Unspecified
                )
                Spacer(modifier = Modifier.width(12.dp))
                Text(
                    text = "Continue with Google",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Normal
                )
            }
        }

        // Terms and Privacy Policy text with clickable links
        ClickableTermsText(
            onTermsClicked = onTermsClicked,
            modifier = Modifier
                .fillMaxWidth()
                .padding(top = 24.dp)
        )
    }
}

