package com.example.foodiemate.ui

import android.content.Intent
import android.os.Bundle
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import com.example.foodiemate.R
import com.google.android.material.card.MaterialCardView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        findViewById<MaterialCardView>(R.id.cardWheel).setOnClickListener {
            startActivity(Intent(this, WheelActivity::class.java))
        }

        findViewById<MaterialCardView>(R.id.cardDiary).setOnClickListener {
            startActivity(Intent(this, DiaryActivity::class.java))
        }

        // Placeholders for other features
        findViewById<MaterialCardView>(R.id.cardMap).setOnClickListener {
            Toast.makeText(this, "地图周边餐饮功能开发中", Toast.LENGTH_SHORT).show()
        }

        findViewById<MaterialCardView>(R.id.cardRecommend).setOnClickListener {
            Toast.makeText(this, "智能推荐功能开发中", Toast.LENGTH_SHORT).show()
        }

        findViewById<MaterialCardView>(R.id.cardRecipes).setOnClickListener {
            Toast.makeText(this, "食谱大全功能开发中", Toast.LENGTH_SHORT).show()
        }
    }
}
