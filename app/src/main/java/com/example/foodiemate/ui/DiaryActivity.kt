package com.example.foodiemate.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.example.foodiemate.R

class DiaryActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_diary)
        
        supportActionBar?.title = "美食日记"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressed()
        return true
    }
}

