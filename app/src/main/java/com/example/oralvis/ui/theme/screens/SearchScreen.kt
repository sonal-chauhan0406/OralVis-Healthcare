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
fun SearchScreen(vm: SessionViewModel, onBack: () -> Unit) {
    var sessionId by remember { mutableStateOf("") }
    val selected by vm.selectedSession.collectAsState()

    Scaffold(topBar = { TopAppBar(title = { Text("Search") }) }) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            OutlinedTextField(value = sessionId, onValueChange = { sessionId = it }, label = { Text("SessionID") }, modifier = Modifier.fillMaxWidth())
            Spacer(Modifier.height(12.dp))
            Button(onClick = { if (sessionId.isNotBlank()) vm.fetchSession(sessionId.trim()) }, modifier = Modifier.fillMaxWidth()) {
                Text("Search")
            }
            Spacer(Modifier.height(16.dp))
            selected?.let { s ->
                Text("Session: ${s.sessionId}", style = MaterialTheme.typography.titleMedium)
                Text("Name: ${s.name}, Age: ${s.age}")
                Spacer(Modifier.height(8.dp))
                Text("Images:", style = MaterialTheme.typography.titleSmall)
                Column {
                    s.imagePaths.forEach { p ->
                        Card(Modifier.fillMaxWidth().padding(vertical = 6.dp)) {
                            Row(Modifier.padding(8.dp)) {
                                AsyncImage(model = File(p), contentDescription = null, modifier = Modifier.size(120.dp))
                                Spacer(Modifier.width(8.dp))
                                Column { Text(File(p).name); Text(p, style = MaterialTheme.typography.bodySmall) }
                            }
                        }
                    }
                }
            } ?: Text("No session loaded.")
            Spacer(Modifier.height(12.dp))
            Button(onClick = onBack, modifier = Modifier.fillMaxWidth()) { Text("Back") }
        }
    }
}
