package com.example.oralvis.ui.screens

import android.Manifest
import android.content.pm.PackageManager
import androidx.activity.compose.rememberLauncherForActivityResult
import androidx.activity.result.contract.ActivityResultContracts
import androidx.camera.core.ImageCapture
import androidx.camera.core.ImageCaptureException
import androidx.camera.lifecycle.ProcessCameraProvider
import androidx.compose.foundation.layout.*
import androidx.compose.foundation.lazy.LazyRow
import androidx.compose.foundation.lazy.items
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.platform.LocalContext
import androidx.compose.ui.platform.LocalLifecycleOwner
import androidx.compose.ui.unit.dp
import androidx.compose.ui.viewinterop.AndroidView
import androidx.core.content.ContextCompat
import coil.compose.AsyncImage
import com.example.oralvis.data.db.SessionEntity
import com.example.oralvis.utils.FileUtils
import com.example.oralvis.viewmodel.SessionViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun CaptureScreen(vm: SessionViewModel, onBack: () -> Unit) {
    val context = LocalContext.current
    val lifecycleOwner = LocalLifecycleOwner.current
    val imageCapture = remember { ImageCapture.Builder().setCaptureMode(ImageCapture.CAPTURE_MODE_MINIMIZE_LATENCY).build() }

    val permissionLauncher = rememberLauncherForActivityResult(ActivityResultContracts.RequestPermission()) { granted ->
        // no-op here; UI will adapt if not granted
    }

    LaunchedEffect(Unit) {
        if (ContextCompat.checkSelfPermission(context, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED) {
            permissionLauncher.launch(Manifest.permission.CAMERA)
        }
    }

    var sessionActive by remember { mutableStateOf(false) }
    var tempSessionId by remember { mutableStateOf("") }
    val capturedPaths = remember { mutableStateListOf<String>() }
    var showEndDialog by remember { mutableStateOf(false) }

    Scaffold(topBar = {
        TopAppBar(title = { Text("Capture Images") }, navigationIcon = { IconButton(onClick = onBack) { Text("Back") } })
    }) { padding ->
        Column(Modifier.padding(padding).fillMaxSize(), horizontalAlignment = Alignment.CenterHorizontally) {
            // Camera preview container
            AndroidView(modifier = Modifier
                .fillMaxWidth()
                .weight(1f),
                factory = { ctx ->
                    val previewView = androidx.camera.view.PreviewView(ctx)
                    val cameraProviderFuture = ProcessCameraProvider.getInstance(ctx)
                    cameraProviderFuture.addListener({
                        val cameraProvider = cameraProviderFuture.get()
                        val preview = androidx.camera.core.Preview.Builder().build().also { it.setSurfaceProvider(previewView.surfaceProvider) }
                        try {
                            cameraProvider.unbindAll()
                            cameraProvider.bindToLifecycle(lifecycleOwner, androidx.camera.core.CameraSelector.DEFAULT_BACK_CAMERA, preview, imageCapture)
                        } catch (e: Exception) { e.printStackTrace() }
                    }, ContextCompat.getMainExecutor(ctx))
                    previewView
                }
            )

            Spacer(Modifier.height(12.dp))

            Row(Modifier.fillMaxWidth().padding(horizontal = 12.dp), horizontalArrangement = Arrangement.SpaceBetween) {
                Button(onClick = {
                    if (!sessionActive) {
                        sessionActive = true
                        tempSessionId = "temp_${System.currentTimeMillis()}"
                    }
                }) { Text(if (!sessionActive) "Start Session" else "Session Active") }

                Button(onClick = {
                    if (!sessionActive) return@Button
                    // create file and capture
                    val outFile: File = FileUtils.createImageFile(context, tempSessionId)
                    val outputOptions = ImageCapture.OutputFileOptions.Builder(outFile).build()
                    imageCapture.takePicture(outputOptions, ContextCompat.getMainExecutor(context), object : ImageCapture.OnImageSavedCallback {
                        override fun onImageSaved(outputFileResults: ImageCapture.OutputFileResults) {
                            capturedPaths.add(outFile.absolutePath)
                        }
                        override fun onError(exception: ImageCaptureException) { exception.printStackTrace() }
                    })
                }) { Text("Capture") }

                Button(onClick = {
                    if (!sessionActive) return@Button
                    showEndDialog = true
                }) { Text("End Session") }
            }

            Spacer(Modifier.height(12.dp))

            if (capturedPaths.isNotEmpty()) {
                LazyRow(Modifier.fillMaxWidth().padding(8.dp)) {
                    items(capturedPaths) { p ->
                        Card(Modifier.padding(8.dp)) {
                            AsyncImage(model = File(p), contentDescription = null, modifier = Modifier.size(120.dp))
                        }
                    }
                }
            } else {
                Text("No images captured yet.", Modifier.padding(12.dp))
            }
        }
    }

    if (showEndDialog) {
        EndSessionDialog(
            onDismiss = { showEndDialog = false },
            onSave = { sessionId, name, age ->
                val movedPaths = FileUtils.moveSessionFiles(context, tempSessionId, sessionId)
                val entity = SessionEntity(sessionId = sessionId, name = name, age = age, imagePaths = movedPaths)
                vm.insertSession(entity)
                // reset
                capturedPaths.clear()
                sessionActive = false
                tempSessionId = ""
                showEndDialog = false
            }
        )
    }
}
