package com.mason.myapplication

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query

@Dao
interface RecordDao {
    @Insert
    fun insert(record: Record)

    @Query("SELECT * FROM Record")
    fun getAll(): List<Record>
}
