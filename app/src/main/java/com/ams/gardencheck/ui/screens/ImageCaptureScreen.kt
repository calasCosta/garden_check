package com.ams.gardencheck.ui.screens

import androidx.compose.material3.Text
import androidx.compose.runtime.Composable
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.sp
import androidx.navigation.NavController
import com.ams.gardencheck.ui.main.MainViewModel

@Composable
fun ImageCaptureScreen(modifier: Modifier = Modifier,
                       mv: MainViewModel,
                       nc: NavController){
    Text(
        text = "Image Capture Screen",
        fontSize = 32.sp,
    )
}