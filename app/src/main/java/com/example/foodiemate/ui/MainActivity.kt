package com.example.foodiemate.ui

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import com.example.foodiemate.R
import com.example.foodiemate.ui.fragment.DiaryFragment
import com.example.foodiemate.ui.fragment.HomeFragment
import com.example.foodiemate.ui.fragment.MapFragment
import com.example.foodiemate.ui.fragment.RecipesFragment
import com.google.android.material.bottomnavigation.BottomNavigationView

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        val navView = findViewById<BottomNavigationView>(R.id.nav_view)

        // Set default fragment
        if (savedInstanceState == null) {
            replaceFragment(HomeFragment())
        }

        navView.setOnItemSelectedListener { item ->
            when (item.itemId) {
                R.id.navigation_home -> {
                    replaceFragment(HomeFragment())
                    true
                }
                R.id.navigation_diary -> {
                    replaceFragment(DiaryFragment())
                    true
                }
                R.id.navigation_map -> {
                    replaceFragment(MapFragment())
                    true
                }
                R.id.navigation_recipes -> {
                    replaceFragment(RecipesFragment())
                    true
                }
                else -> false
            }
        }
    }

    private fun replaceFragment(fragment: Fragment) {
        supportFragmentManager.beginTransaction()
            .replace(R.id.nav_host_fragment, fragment)
            .commit()
    }
}
