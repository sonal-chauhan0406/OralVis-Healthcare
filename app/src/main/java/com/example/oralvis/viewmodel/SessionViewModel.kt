package com.example.oralvis.viewmodel

import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.oralvis.data.db.SessionEntity
import com.example.oralvis.data.repository.SessionRepository
import kotlinx.coroutines.flow.MutableStateFlow
import kotlinx.coroutines.flow.StateFlow
import kotlinx.coroutines.launch

class SessionViewModel(private val repository: SessionRepository) : ViewModel() {

    private val _sessions = MutableStateFlow<List<SessionEntity>>(emptyList())
    val sessions: StateFlow<List<SessionEntity>> = _sessions

    private val _selectedSession = MutableStateFlow<SessionEntity?>(null)
    val selectedSession: StateFlow<SessionEntity?> = _selectedSession

    init {
        observeAll()
    }

    private fun observeAll() {
        viewModelScope.launch {
            repository.getAllSessions().collect { list -> _sessions.value = list }
        }
    }

    fun insertSession(session: SessionEntity) {
        viewModelScope.launch { repository.insertSession(session) }
    }

    fun fetchSession(sessionId: String) {
        viewModelScope.launch {
            repository.getSessionById(sessionId).collect { _selectedSession.value = it }
        }
    }
}
