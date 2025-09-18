package com.example.oralvis.ui.screens

import androidx.compose.foundation.layout.Column
import androidx.compose.foundation.layout.fillMaxWidth
import androidx.compose.material3.AlertDialog
import androidx.compose.material3.OutlinedTextField
import androidx.compose.material3.Text
import androidx.compose.material3.TextButton
import androidx.compose.runtime.*
import androidx.compose.ui.Modifier

@Composable
fun EndSessionDialog(onDismiss: () -> Unit, onSave: (String, String, Int) -> Unit) {
    var sid by remember { mutableStateOf("") }
    var name by remember { mutableStateOf("") }
    var ageText by remember { mutableStateOf("") }

    AlertDialog(
        onDismissRequest = onDismiss,
        title = { Text("End Session - Enter metadata") },
        text = {
            Column {
                OutlinedTextField(value = sid, onValueChange = { sid = it }, label = { Text("Session ID") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = name, onValueChange = { name = it }, label = { Text("Name") }, modifier = Modifier.fillMaxWidth())
                OutlinedTextField(value = ageText, onValueChange = { ageText = it }, label = { Text("Age") }, modifier = Modifier.fillMaxWidth())
            }
        },
        confirmButton = {
            TextButton(onClick = {
                val age = ageText.toIntOrNull() ?: 0
                if (sid.isNotBlank()) onSave(sid.trim(), name.trim(), age)
            }) { Text("Save") }
        },
        dismissButton = {
            TextButton(onClick = onDismiss) { Text("Cancel") }
        }
    )
}
