package com.example.savageexcuse.data

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "excuses")
data class Excuse(
    @PrimaryKey(autoGenerate = true) val id: Int = 0,
    val category: String,
    val text: String,
    val isFavorite: Boolean = false
)
