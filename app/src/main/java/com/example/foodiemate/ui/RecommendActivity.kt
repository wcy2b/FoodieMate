package com.example.foodiemate.ui

import android.os.Bundle
import android.view.View
import android.widget.Toast
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.foodiemate.R
import com.example.foodiemate.data.api.OverpassResponse
import com.example.foodiemate.data.api.RetrofitClient
import com.example.foodiemate.data.model.RestaurantRecommendation
import com.example.foodiemate.ui.adapter.RecommendAdapter
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import kotlin.random.Random

class RecommendActivity : AppCompatActivity() {

    private lateinit var adapter: RecommendAdapter
    
    // Fake images for demo (Expanded list to avoid duplicates)
    private val foodImages = listOf(
        "https://images.unsplash.com/photo-1555126634-323283e090fa?w=400",
        "https://images.unsplash.com/photo-1517248135467-4c7edcad34c4?w=400",
        "https://images.unsplash.com/photo-1540189549336-e6e99c3679fe?w=400",
        "https://images.unsplash.com/photo-1565299624946-b28f40a0ae38?w=400",
        "https://images.unsplash.com/photo-1484723091739-30a097e8f929?w=400",
        "https://images.unsplash.com/photo-1482049016688-2d3e1b311543?w=400",
        "https://images.unsplash.com/photo-1467003909585-2f8a7270028d?w=400",
        "https://images.unsplash.com/photo-1473093295043-cdd812d0e601?w=400",
        "https://images.unsplash.com/photo-1496417263034-38ec4f0d665a?w=400",
        "https://images.unsplash.com/photo-1504674900247-0877df9cc836?w=400",
        "https://images.unsplash.com/photo-1506354666786-959d6d497f1a?w=400",
        "https://images.unsplash.com/photo-1512621776951-a57141f2eefd?w=400",
        "https://images.unsplash.com/photo-1476224203421-9ac39bcb3327?w=400",
        "https://images.unsplash.com/photo-1493770348161-369560ae357d?w=400",
        "https://images.unsplash.com/photo-1499028344343-cd173ffc68a9?w=400",
        "https://images.unsplash.com/photo-1455619452474-d2be8b1e70cd?w=400",
        "https://images.unsplash.com/photo-1490645935967-10de6ba17061?w=400",
        "https://images.unsplash.com/photo-1504754524776-3f4f30526641?w=400",
        "https://images.unsplash.com/photo-1498837167922-ddd27525d352?w=400",
        "https://images.unsplash.com/photo-1505253149613-112d21d9f6a9?w=400"
    )

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_recommend)

        supportActionBar?.title = "每日推荐"
        supportActionBar?.setDisplayHomeAsUpEnabled(true)

        setupRecyclerView()
        fetchRecommendations()
    }

    private fun setupRecyclerView() {
        val recyclerView = findViewById<RecyclerView>(R.id.rvRecommend)
        adapter = RecommendAdapter()
        recyclerView.adapter = adapter
        recyclerView.layoutManager = LinearLayoutManager(this)
    }

    private fun fetchRecommendations() {
        // Query restaurants around Beijing default location
        val lat = 39.9042
        val lon = 116.4074
        val radius = 2000 // 2km radius
        val query = "[out:json];node[\"amenity\"=\"restaurant\"](around:$radius,$lat,$lon);out;"

        RetrofitClient.instance.searchNearbyRestaurants(query).enqueue(object : Callback<OverpassResponse> {
            override fun onResponse(call: Call<OverpassResponse>, response: Response<OverpassResponse>) {
                if (response.isSuccessful && response.body() != null) {
                    val elements = response.body()!!.elements.take(20) // Limit to 20
                    val recommendations = elements.mapIndexed { index, element ->
                        val name = element.tags?.get("name") ?: "未命名餐厅"
                        val cuisine = element.tags?.get("cuisine") ?: "综合美食"
                        
                        // Use index to pick image to avoid duplicates as much as possible
                        val imageIndex = index % foodImages.size
                        
                        // Enrich with mock data for UI
                        RestaurantRecommendation(
                            name = name,
                            rating = String.format("%.1f", Random.nextDouble(3.5, 5.0)).toFloat(),
                            price = "¥${Random.nextInt(30, 300)}/人",
                            distance = "${Random.nextInt(100, 2000)}m",
                            imageUrl = foodImages[imageIndex],
                            type = cuisine
                        )
                    }
                    adapter.submitList(recommendations)
                } else {
                    Toast.makeText(this@RecommendActivity, "获取推荐失败", Toast.LENGTH_SHORT).show()
                }
            }

            override fun onFailure(call: Call<OverpassResponse>, t: Throwable) {
                Toast.makeText(this@RecommendActivity, "网络错误，请检查网络", Toast.LENGTH_SHORT).show()
            }
        })
    }

    override fun onSupportNavigateUp(): Boolean {
        onBackPressedDispatcher.onBackPressed()
        return true
    }
}

