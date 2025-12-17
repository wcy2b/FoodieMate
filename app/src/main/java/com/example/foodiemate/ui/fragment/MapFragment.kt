package com.example.foodiemate.ui.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import androidx.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import android.widget.Toast
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.foodiemate.R
import com.example.foodiemate.data.api.OverpassResponse
import com.example.foodiemate.data.api.RetrofitClient
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response

class MapFragment : Fragment() {

    private lateinit var map: MapView
    private lateinit var tvStatus: TextView

    // Default location (Beijing - Wangfujing)
    private val defaultLocation = GeoPoint(39.9042, 116.4074)

    private val requestPermissionLauncher =
        registerForActivityResult(ActivityResultContracts.RequestMultiplePermissions()) { permissions ->
            setupMap()
        }

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val config = Configuration.getInstance()
        config.load(
            requireContext(),
            PreferenceManager.getDefaultSharedPreferences(requireContext())
        )
        config.userAgentValue = requireContext().packageName

        return inflater.inflate(R.layout.fragment_map, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        
        tvStatus = view.findViewById(R.id.tvStatus)
        map = view.findViewById(R.id.map)
        
        // Custom Gaode Tile Source
        val gaodeTileSource = object : OnlineTileSourceBase(
            "Gaode",
            1,
            19,
            256,
            ".png",
            arrayOf(
                "https://wprd01.is.autonavi.com/appmaptile?",
                "https://wprd02.is.autonavi.com/appmaptile?",
                "https://wprd03.is.autonavi.com/appmaptile?"
            )
        ) {
            override fun getTileURLString(pMapTileIndex: Long): String {
                val url = baseUrl
                val x = MapTileIndex.getX(pMapTileIndex)
                val y = MapTileIndex.getY(pMapTileIndex)
                val z = MapTileIndex.getZoom(pMapTileIndex)
                return "${url}lang=zh_cn&size=1&scale=1&style=7&x=$x&y=$y&z=$z"
            }
        }

        map.setTileSource(gaodeTileSource)
        map.setMultiTouchControls(true)

        checkPermissions()
    }

    private fun checkPermissions() {
        if (ContextCompat.checkSelfPermission(
                requireContext(),
                Manifest.permission.ACCESS_FINE_LOCATION
            ) != PackageManager.PERMISSION_GRANTED
        ) {
            requestPermissionLauncher.launch(
                arrayOf(
                    Manifest.permission.ACCESS_FINE_LOCATION,
                    Manifest.permission.ACCESS_COARSE_LOCATION
                )
            )
        } else {
            setupMap()
        }
    }

    private fun setupMap() {
        tvStatus.text = "正在搜索周边真实餐饮..."
        
        val mapController = map.controller
        mapController.setZoom(17.0)
        
        val startPoint = defaultLocation
        mapController.setCenter(startPoint)

        val userMarker = Marker(map)
        userMarker.position = startPoint
        userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        userMarker.title = "当前位置"
        userMarker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_marker_user)
        map.overlays.add(userMarker)
        map.invalidate()

        // Fetch Real Data from Overpass API
        fetchRealRestaurants(startPoint)
    }

    private fun fetchRealRestaurants(center: GeoPoint) {
        // Query: node["amenity"="restaurant"](around:radius,lat,lon);out;
        val radius = 800 // meters
        val query = "[out:json];node[\"amenity\"=\"restaurant\"](around:$radius,${center.latitude},${center.longitude});out;"

        RetrofitClient.instance.searchNearbyRestaurants(query).enqueue(object : Callback<OverpassResponse> {
            override fun onResponse(call: Call<OverpassResponse>, response: Response<OverpassResponse>) {
                if (!isAdded || context == null) return // Safety check

                if (response.isSuccessful && response.body() != null) {
                    val elements = response.body()!!.elements
                    if (elements.isNotEmpty()) {
                        tvStatus.text = "找到 ${elements.size} 家周边真实餐饮"
                        for (element in elements) {
                            val name = element.tags?.get("name") ?: "未知餐厅"
                            val cuisine = element.tags?.get("cuisine") ?: "美食"
                            addRestaurantMarker(GeoPoint(element.lat, element.lon), name, cuisine)
                        }
                        map.invalidate()
                    } else {
                        tvStatus.text = "周边暂无数据，显示推荐餐厅"
                        addMockData()
                    }
                } else {
                    tvStatus.text = "数据获取失败，显示推荐餐厅"
                    addMockData()
                }
            }

            override fun onFailure(call: Call<OverpassResponse>, t: Throwable) {
                if (!isAdded || context == null) return // Safety check
                tvStatus.text = "网络错误: ${t.message}，显示推荐餐厅"
                addMockData()
            }
        })
    }

    private fun addMockData() {
        addRestaurantMarker(GeoPoint(39.9045, 116.4078), "全聚德烤鸭店", "人均 ¥180 | 烤鸭")
        addRestaurantMarker(GeoPoint(39.9038, 116.4070), "东来顺饭庄", "人均 ¥120 | 涮羊肉")
        addRestaurantMarker(GeoPoint(39.9048, 116.4068), "四季民福烤鸭店", "人均 ¥150 | 京菜")
        addRestaurantMarker(GeoPoint(39.9040, 116.4082), "小吊梨汤", "人均 ¥90 | 私房菜")
        addRestaurantMarker(GeoPoint(39.9035, 116.4065), "麦当劳(王府井店)", "人均 ¥40 | 快餐")
        map.invalidate()
    }

    private fun addRestaurantMarker(position: GeoPoint, name: String, snippet: String) {
        val marker = Marker(map)
        marker.position = position
        marker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        marker.title = name
        marker.snippet = snippet
        marker.subDescription = "点击查看详情"
        marker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_marker_restaurant)
        map.overlays.add(marker)
    }

    override fun onResume() {
        super.onResume()
        map.onResume()
    }

    override fun onPause() {
        super.onPause()
        map.onPause()
    }
}
