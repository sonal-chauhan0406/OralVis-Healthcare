package com.example.oralvis.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import coil.compose.AsyncImage
import com.example.oralvis.viewmodel.SessionViewModel
import java.io.File

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun DetailScreen(vm: SessionViewModel, sessionId: String, onBack: () -> Unit) {
    LaunchedEffect(sessionId) { vm.fetchSession(sessionId) }
    val selected by vm.selectedSession.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Details") }) }) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            selected?.let { s ->
                Text("${s.sessionId} — ${s.name}, ${s.age}")
                Spacer(Modifier.height(8.dp))
                s.imagePaths.forEach { p ->
                    Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                        Row(Modifier.padding(8.dp)) {
                            AsyncImage(model = File(p), contentDescription = null, modifier = Modifier.size(120.dp))
                            Spacer(Modifier.width(8.dp))
                            Column { Text(File(p).name); Text(p, style = MaterialTheme.typography.bodySmall) }
                        }
                    }
                }
            } ?: Text("Loading...")
            Spacer(Modifier.height(12.dp))
            Button(onClick = onBack) { Text("Back") }
        }
    }
}
