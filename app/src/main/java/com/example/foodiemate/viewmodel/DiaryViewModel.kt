package com.example.foodiemate.viewmodel

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.viewModelScope
import com.example.foodiemate.data.AppDatabase
import com.example.foodiemate.data.entity.Diary
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch

class DiaryViewModel(application: Application) : AndroidViewModel(application) {
    
    private val diaryDao = AppDatabase.getDatabase(application).diaryDao()
    val allDiaries: LiveData<List<Diary>> = diaryDao.getAllDiaries()

    fun insert(diary: Diary) = viewModelScope.launch(Dispatchers.IO) {
        diaryDao.insertDiary(diary)
    }

    fun delete(diary: Diary) = viewModelScope.launch(Dispatchers.IO) {
        diaryDao.deleteDiary(diary)
    }
}

