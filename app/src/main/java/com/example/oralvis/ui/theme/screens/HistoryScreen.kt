package com.example.oralvis.ui.screens

import androidx.compose.foundation.clickable
import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.oralvis.viewmodel.SessionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HistoryScreen(vm: SessionViewModel, onBack: () -> Unit, onOpenDetail: (String) -> Unit) {
    val sessions by vm.sessions.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("History") }) }) { padding ->
        Column(Modifier.padding(padding).padding(16.dp)) {
            sessions.forEach { s ->
                Card(Modifier
                    .fillMaxWidth()
                    .padding(vertical = 4.dp)
                    .clickable { onOpenDetail(s.sessionId) }) {
                    Column(Modifier.padding(12.dp)) {
                        Text("${s.sessionId} — ${s.name}")
                        Text("Images: ${s.imagePaths.size}")
                    }
                }
            }
            Spacer(Modifier.height(12.dp))
            Button(onClick = onBack) { Text("Back") }
        }
    }
}
