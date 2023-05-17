package com.mason.myapplication.guessnumber

import androidx.annotation.NonNull
import androidx.room.ColumnInfo
import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity
class Record(
    @NonNull
    @ColumnInfo(name = "name")
    val name: String,
    @NonNull
    val counter: Int) {
    @PrimaryKey(autoGenerate = true)
    var id : Long = 0
}