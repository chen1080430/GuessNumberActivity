package com.mason.myapplication

import androidx.room.Database
import androidx.room.RoomDatabase

@Database(entities = arrayOf(Record::class), version = 1)
abstract class GameDatabases : RoomDatabase() {
    abstract fun recordDao(): RecordDao
}