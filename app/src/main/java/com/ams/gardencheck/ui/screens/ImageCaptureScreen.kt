package com.ams.gardencheck.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.*
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.camera.view.PreviewView
import androidx.compose.foundation.background
import androidx.compose.foundation.border
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.shape.CircleShape
import androidx.compose.foundation.shape.RoundedCornerShape
import androidx.compose.material.icons.Icons
import androidx.compose.material.icons.filled.Home
import androidx.compose.material3.Icon
import androidx.compose.material3.IconButton
import androidx.compose.material3.Text
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.graphics.Color
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.res.painterResource
import androidx.compose.ui.text.font.FontWeight
import androidx.compose.ui.unit.dp
import androidx.compose.ui.unit.sp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import com.ams.gardencheck.R
import java.io.File

@Composable
fun ImageCaptureScreen(
    modifier: Modifier = Modifier,
    onHomeClick: () -> Unit = {}
) {
    val context = LocalContext.current

    var hasCameraPermission by remember {
        mutableStateOf(
            ContextCompat.checkSelfPermission(
                context,
                Manifest.permission.CAMERA
            ) == PackageManager.PERMISSION_GRANTED
        )
    }

    val permissionLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.RequestPermission()
        ) { granted ->
            hasCameraPermission = granted
        }

    // Ask permission only when screen first appears
    LaunchedEffect(Unit) {
        if (!hasCameraPermission) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    Box(modifier = modifier.fillMaxSize()) {
        if (hasCameraPermission) {
            CameraContent(
                modifier = Modifier.fillMaxSize(),
                onHomeClick = onHomeClick
            )
        } else {
            PermissionPlaceholder()
        }
    }
}

@Composable
private fun CameraContent(
    modifier: Modifier,
    onHomeClick: () -> Unit
) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current

    var imageCapture: ImageCapture? by remember { mutableStateOf(null) }

    val cameraProviderFuture = remember {
        ProcessCameraProvider.getInstance(context)
    }

    val galleryLauncher =
        rememberLauncherForActivityResult(
            ActivityResultContracts.GetContent()
        ) { /* handle gallery image */ }

    Box(modifier = modifier) {

        /** CAMERA PREVIEW (edge-to-edge) **/
        AndroidView(
            modifier = Modifier.fillMaxSize(),
            factory = { ctx ->
                val previewView = PreviewView(ctx)

                val preview = Preview.Builder().build()
                val capture = ImageCapture.Builder().build()
                imageCapture = capture

                val selector = CameraSelector.DEFAULT_BACK_CAMERA

                cameraProviderFuture.get().apply {
                    unbindAll()
                    bindToLifecycle(
                        lifecycleOwner,
                        selector,
                        preview,
                        capture
                    )
                    preview.setSurfaceProvider(previewView.surfaceProvider)
                }
                previewView
            }
        )

        /** SAFE UI OVERLAY **/
        Box(
            modifier = Modifier
                .fillMaxSize()
                .windowInsetsPadding(WindowInsets.safeDrawing)
        ) {

            /** TOP INSTRUCTION **/
            Box(
                modifier = Modifier
                    .align(Alignment.TopCenter)
                    .padding(top = 16.dp)
                    .background(Color.White, RoundedCornerShape(50))
                    .padding(horizontal = 24.dp, vertical = 12.dp)
            ) {
                Text(
                    text = "Position the leaf within the frame",
                    fontSize = 16.sp,
                    fontWeight = FontWeight.Medium,
                    color = Color.Black
                )
            }

            /** CENTER FRAME **/
            Box(
                modifier = Modifier
                    .size(260.dp)
                    .align(Alignment.Center)
                    .border(
                        width = 3.dp,
                        color = Color.DarkGray,
                        shape = RoundedCornerShape(16.dp)
                    )
            )

            /** BOTTOM CONTROLS **/
            Row(
                modifier = Modifier
                    .fillMaxWidth()
                    .align(Alignment.BottomCenter)
                    .padding(bottom = 16.dp),
                horizontalArrangement = Arrangement.SpaceEvenly,
                verticalAlignment = Alignment.CenterVertically
            ) {

                /** GALLERY **/
                IconButton(
                    onClick = { galleryLauncher.launch("image/*") },
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icons8_gallery_48),
                        contentDescription = "Gallery",
                        tint = Color.Black
                    )
                }

                /** SHUTTER **/
                IconButton(
                    onClick = {
                        val photoFile =
                            File(context.cacheDir, "${System.currentTimeMillis()}.jpg")

                        val outputOptions =
                            ImageCapture.OutputFileOptions.Builder(photoFile).build()

                        imageCapture?.takePicture(
                            outputOptions,
                            ContextCompat.getMainExecutor(context),
                            object : ImageCapture.OnImageSavedCallback {
                                override fun onImageSaved(
                                    outputFileResults: ImageCapture.OutputFileResults
                                ) {
                                    // handle saved photo
                                }

                                override fun onError(exception: ImageCaptureException) {}
                            }
                        )
                    },
                    modifier = Modifier
                        .size(80.dp)
                        .background(Color(0xFF16A34A), CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icons8_camera_50),
                        contentDescription = "Capture",
                        tint = Color.White,
                        modifier = Modifier.size(36.dp)
                    )
                }

                /** HOME **/
                IconButton(
                    onClick = onHomeClick,
                    modifier = Modifier
                        .size(56.dp)
                        .background(Color.White, CircleShape)
                ) {
                    Icon(
                        painter = painterResource(id = R.drawable.icons8_home_50),
                        contentDescription = "Home",
                        tint = Color.Black
                    )
                }
            }
        }
    }
}

@Composable
private fun PermissionPlaceholder() {
    Box(
        modifier = Modifier.fillMaxSize(),
        contentAlignment = Alignment.Center
    ) {
        Text(
            text = "Camera permission required",
            fontSize = 16.sp
        )
    }
}


