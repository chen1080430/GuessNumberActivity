package com.mason.myapplication.guessnumber

import androidx.room.Dao
import androidx.room.Insert
import androidx.room.Query
import com.mason.myapplication.guessnumber.Record

@Dao
interface RecordDao {
    @Insert
    fun insert(record: Record)

    @Query("SELECT * FROM Record")
    fun getAll(): List<Record>
}
