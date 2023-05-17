package com.mason.myapplication.guessnumber

import androidx.room.Database
import androidx.room.RoomDatabase
import com.mason.myapplication.guessnumber.Record
import com.mason.myapplication.guessnumber.RecordDao

@Database(entities = arrayOf(Record::class), version = 1)
abstract class GameDatabases : RoomDatabase() {
    abstract fun recordDao(): RecordDao
}