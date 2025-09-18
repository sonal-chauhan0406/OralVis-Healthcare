package com.example.oralvis.ui.screens

import androidx.compose.foundation.layout.*
import androidx.compose.material3.*
import androidx.compose.runtime.*
import androidx.compose.ui.Alignment
import androidx.compose.ui.Modifier
import androidx.compose.ui.unit.dp
import com.example.oralvis.viewmodel.SessionViewModel

@OptIn(ExperimentalMaterial3Api::class)
@Composable
fun HomeScreen(
    vm: SessionViewModel,
    onStartSession: () -> Unit,
    onSearch: () -> Unit,
    onHistory: () -> Unit,
    onSettings: () -> Unit
) {
    val sessions by vm.sessions.collectAsState()
    Scaffold(topBar = { TopAppBar(title = { Text("OralVis") }) }) { padding ->
        Column(
            Modifier
                .padding(padding)
                .padding(16.dp)
                .fillMaxSize(),
            verticalArrangement = Arrangement.spacedBy(12.dp),
            horizontalAlignment = Alignment.CenterHorizontally
        ) {
            Button(onClick = onStartSession, modifier = Modifier.fillMaxWidth()) { Text("Start Session") }
            Button(onClick = onSearch, modifier = Modifier.fillMaxWidth()) { Text("Search") }
            Button(onClick = onHistory, modifier = Modifier.fillMaxWidth()) { Text("History") }
            Button(onClick = onSettings, modifier = Modifier.fillMaxWidth()) { Text("Settings") }

            Divider()
            Text("Recent Sessions:", style = MaterialTheme.typography.titleMedium)
            Spacer(Modifier.height(8.dp))
            if (sessions.isEmpty()) {
                Box(Modifier.fillMaxWidth(), contentAlignment = Alignment.Center) { Text("No sessions yet") }
            } else {
                Column {
                    sessions.forEach { s ->
                        Text("• ${s.sessionId} — ${s.name}, ${s.age}")
                    }
                }
            }
        }
    }
}
