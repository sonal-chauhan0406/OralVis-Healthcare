package com.example.oralvis.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import androidx.room.TypeConverters

@Database(entities = [SessionEntity::class], version = 1, exportSchema = false)
@TypeConverters(Converters::class)
abstract class OralVisDatabase : RoomDatabase() {
    abstract fun sessionDao(): SessionDao
}
