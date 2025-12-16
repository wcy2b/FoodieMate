package com.example.foodiemate.data.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "diary_table")
data class Diary(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val foodName: String,
    val content: String,
    val date: String, // Format: yyyy-MM-dd
    val imageUri: String? = null // For future image support
)

