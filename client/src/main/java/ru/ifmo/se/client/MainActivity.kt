package ru.ifmo.se.client

import android.Manifest
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.Bundle
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import com.here.android.mpa.common.*
import com.here.android.mpa.mapping.Map
import com.here.android.mpa.mapping.MapMarker
import com.here.android.mpa.mapping.SupportMapFragment
import java.io.File


class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_ASK_PERMISSIONS = 1
    private val REQUIRED_SDK_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private val INTENT_NAME = "INIT_MAP"

    private lateinit var map: Map
    private lateinit var mapFragment: SupportMapFragment
    //ToDo: take coordinates from server
    private lateinit var musicians: List<GeoCoordinate>

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions()
    }

    private fun checkPermissions() {
        val missingPermissions = ArrayList<String>()
        REQUIRED_SDK_PERMISSIONS.forEach {
            val result = ContextCompat.checkSelfPermission(this, it)
            if (result != PackageManager.PERMISSION_GRANTED) {
                missingPermissions.add(it)
            }
        }

        if (!missingPermissions.isEmpty()) {
            val permissions = missingPermissions.toTypedArray()
            ActivityCompat.requestPermissions(this, permissions, REQUEST_CODE_ASK_PERMISSIONS)
        }
        initMap()
    }

    private fun initMap() {
        mapFragment = supportFragmentManager.findFragmentById(R.id.mapfragment) as SupportMapFragment

        val success = MapSettings.setIsolatedDiskCacheRootPath(applicationContext.getExternalFilesDir(null).absolutePath +
                File.separator + ".here-maps", INTENT_NAME)
        if(success){
            mapFragment.init {
                if (it == OnEngineInitListener.Error.NONE) {
                    map = mapFragment.map
                    map.setCenter(GeoCoordinate(59.9343, 30.3351), Map.Animation.NONE)
                    map.zoomLevel = (map.maxZoomLevel + map.minZoomLevel) / 2

                    PositioningManager.getInstance().start(PositioningManager.LocationMethod.GPS_NETWORK)
                    mapFragment.positionIndicator.isVisible = true

                    val drawable = resources.getDrawable(R.drawable.musician, theme)
                    val musicianIcon = Bitmap.createBitmap(
                        drawable.intrinsicWidth,
                        drawable.intrinsicHeight, Bitmap.Config.ARGB_8888
                    )
                    val canvas = Canvas(musicianIcon)
                    drawable.setBounds(0, 0, canvas.width, canvas.height)
                    drawable.draw(canvas)

                    val musiciansMarkers = ArrayList<MapMarker>()
                    musicians = arrayListOf(GeoCoordinate(59.9343, 30.3351), GeoCoordinate(59.9340, 30.3348))
                    musicians.forEach {
                        val image = Image()
                        image.bitmap = musicianIcon
                        musiciansMarkers.add(MapMarker(it, image))
                    }
                    map.addMapObjects(musiciansMarkers.toList())
                }
                else {
                    Log.e("map.init", it.name)
                }
            }
        }
    }
}
