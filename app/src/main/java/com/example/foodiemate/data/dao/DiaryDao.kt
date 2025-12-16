package com.example.foodiemate.data.dao

import androidx.lifecycle.LiveData
import androidx.room.Dao
import androidx.room.Delete
import androidx.room.Insert
import androidx.room.OnConflictStrategy
import androidx.room.Query
import com.example.foodiemate.data.entity.Diary

@Dao
interface DiaryDao {
    @Query("SELECT * FROM diary_table ORDER BY id DESC")
    fun getAllDiaries(): LiveData<List<Diary>>

    @Insert(onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertDiary(diary: Diary)

    @Delete
    suspend fun deleteDiary(diary: Diary)
}

