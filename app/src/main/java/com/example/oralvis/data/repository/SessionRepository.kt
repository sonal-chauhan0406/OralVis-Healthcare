package com.example.oralvis.data.repository

import com.example.oralvis.data.db.SessionDao
import com.example.oralvis.data.db.SessionEntity
import kotlinx.coroutines.flow.Flow

class SessionRepository(private val dao: SessionDao) {

    suspend fun insertSession(session: SessionEntity) = dao.insert(session)

    fun getSessionById(sessionId: String): Flow<SessionEntity?> = dao.getSessionById(sessionId)

    fun getAllSessions(): Flow<List<SessionEntity>> = dao.getAllSessions()
}
