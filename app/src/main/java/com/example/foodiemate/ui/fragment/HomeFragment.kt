package com.example.foodiemate.ui.fragment

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.fragment.app.Fragment
import com.example.foodiemate.R
import com.example.foodiemate.ui.widget.FoodWheelView
import com.google.android.material.button.MaterialButton

class HomeFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_home, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tvResult = view.findViewById<TextView>(R.id.tvWheelResult)
        val wheelView = view.findViewById<FoodWheelView>(R.id.foodWheelView)
        val btnSpin = view.findViewById<MaterialButton>(R.id.btnSpin)
        val btnRecommend = view.findViewById<MaterialButton>(R.id.btnRecommend)

        wheelView.setItems(
            listOf(
                "牛肉面", "麻辣烫", "黄焖鸡", "炒饭",
                "饺子", "火锅", "烤肉", "轻食沙拉"
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

        btnRecommend.setOnClickListener {
            Toast.makeText(requireContext(), "智能推荐功能开发中", Toast.LENGTH_SHORT).show()
        }
    }
}

