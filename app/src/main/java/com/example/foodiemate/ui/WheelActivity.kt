package com.example.foodiemate.ui

import android.os.Bundle
import android.widget.TextView
import androidx.appcompat.app.AppCompatActivity
import com.example.foodiemate.R
import com.example.foodiemate.ui.widget.FoodWheelView
import com.google.android.material.button.MaterialButton

class WheelActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_wheel)

        val tvResult = findViewById<TextView>(R.id.tvWheelResult)
        val wheelView = findViewById<FoodWheelView>(R.id.foodWheelView)
        val btnSpin = findViewById<MaterialButton>(R.id.btnSpin)

        wheelView.setItems(
            listOf(
                "牛肉面",
                "麻辣烫",
                "黄焖鸡",
                "炒饭",
                "饺子",
                "火锅",
                "烤肉",
                "轻食沙拉"
            )
        )

        btnSpin.setOnClickListener {
            btnSpin.isEnabled = false
            tvResult.text = getString(R.string.spin_result)

            wheelView.spin { item ->
                tvResult.text = getString(R.string.spin_result) + item
                btnSpin.isEnabled = true
            }
        }
    }
}
