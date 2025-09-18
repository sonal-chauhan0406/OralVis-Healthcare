package com.example.oralvis.data.db

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "sessions")
data class SessionEntity(
    @PrimaryKey val sessionId: String,     // user-provided ID: e.g. "S001"
    val name: String,
    val age: Int,
    val imagePaths: List<String> = emptyList(), // stored via TypeConverter (JSON)
    val timestamp: Long = System.currentTimeMillis()
)
