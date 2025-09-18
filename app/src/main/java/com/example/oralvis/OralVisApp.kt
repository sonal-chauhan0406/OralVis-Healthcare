package com.example.oralvis

import android.app.Application
import androidx.room.Room
import com.example.oralvis.data.db.OralVisDatabase
import com.example.oralvis.data.repository.SessionRepository

class OralVisApp : Application() {
    lateinit var database: OralVisDatabase
        private set

    lateinit var repository: SessionRepository
        private set

    override fun onCreate() {
        super.onCreate()
        database = Room.databaseBuilder(
            applicationContext,
            OralVisDatabase::class.java,
            "oralvis-db"
        )
            .fallbackToDestructiveMigration()
            .build()

        repository = SessionRepository(database.sessionDao())
    }
}
