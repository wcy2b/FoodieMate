package com.example.foodiemate.ui.fragment

import android.Manifest
import android.content.Context
import android.content.pm.PackageManager
import android.os.Bundle
import android.preference.PreferenceManager
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.activity.result.contract.ActivityResultContracts
import androidx.core.content.ContextCompat
import androidx.fragment.app.Fragment
import com.example.foodiemate.R
import org.osmdroid.config.Configuration
import org.osmdroid.tileprovider.tilesource.OnlineTileSourceBase
import org.osmdroid.tileprovider.tilesource.XYTileSource
import org.osmdroid.util.GeoPoint
import org.osmdroid.util.MapTileIndex
import org.osmdroid.views.MapView
import org.osmdroid.views.overlay.Marker

class MapFragment : Fragment() {

    private lateinit var map: MapView
    private lateinit var tvStatus: TextView

    // Default location (Beijing)
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
        // Gaode uses query parameters (x=..&y=..&z=..) instead of standard /z/x/y.png paths
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
                // Construct Gaode specific URL format
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
        tvStatus.text = "周边美食 (OSM SDK + 高德源)"
        
        val mapController = map.controller
        mapController.setZoom(15.0)
        
        val startPoint = defaultLocation
        mapController.setCenter(startPoint)

        // Add User Marker (Blue)
        val userMarker = Marker(map)
        userMarker.position = startPoint
        userMarker.setAnchor(Marker.ANCHOR_CENTER, Marker.ANCHOR_BOTTOM)
        userMarker.title = "我的位置"
        userMarker.icon = ContextCompat.getDrawable(requireContext(), R.drawable.ic_marker_user)
        map.overlays.add(userMarker)

        // Add Realistic Restaurant Markers (Red)
        // Adjust coordinates to be closer and more realistic around Tiananmen/Forbidden City area
        addRestaurantMarker(GeoPoint(39.9082, 116.4024), "全聚德烤鸭店", "人均 ¥180 | 烤鸭")
        addRestaurantMarker(GeoPoint(39.9015, 116.4102), "东来顺饭庄", "人均 ¥120 | 涮羊肉")
        addRestaurantMarker(GeoPoint(39.9120, 116.4050), "四季民福烤鸭店", "人均 ¥150 | 京菜")
        addRestaurantMarker(GeoPoint(39.9060, 116.3980), "小吊梨汤", "人均 ¥90 | 私房菜")
        addRestaurantMarker(GeoPoint(39.9030, 116.4150), "麦当劳(王府井店)", "人均 ¥40 | 快餐")
        
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
