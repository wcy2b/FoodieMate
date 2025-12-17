package com.example.foodiemate.data.entity

import android.os.Parcelable
import androidx.room.Entity
import androidx.room.PrimaryKey
import kotlinx.parcelize.Parcelize

@Parcelize
@Entity(tableName = "diary_table")
data class Diary(
    @PrimaryKey(autoGenerate = true)
    val id: Long = 0,
    val foodName: String,
    val content: String,
    val date: String, // Format: yyyy-MM-dd
    val imageUri: String? = null
) : Parcelable
