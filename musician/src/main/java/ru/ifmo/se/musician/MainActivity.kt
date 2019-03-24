package ru.ifmo.se.musician

import android.Manifest
import android.content.Intent
import android.content.pm.PackageManager
import android.graphics.Bitmap
import android.graphics.Canvas
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.FloatingActionButton
import android.support.v4.app.ActivityCompat
import android.support.v4.content.ContextCompat
import android.support.v7.app.AppCompatActivity
import android.support.v7.widget.LinearLayoutManager
import android.support.v7.widget.RecyclerView
import android.util.Log
import android.widget.Button
import com.here.android.mpa.common.*
import com.here.android.mpa.mapping.Map
import com.here.android.mpa.mapping.MapMarker
import com.here.android.mpa.mapping.SupportMapFragment
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import ru.ifmo.se.protofiles.CommunicatorGrpc
import ru.ifmo.se.protofiles.EmptyMessage
import ru.ifmo.se.protofiles.Musician
import java.io.File
import java.io.PrintWriter
import java.io.StringWriter
import java.util.concurrent.TimeUnit

class MainActivity : AppCompatActivity() {

    private val REQUEST_CODE_ASK_PERMISSIONS = 1
    private val REQUIRED_SDK_PERMISSIONS = arrayOf(
        Manifest.permission.ACCESS_FINE_LOCATION,
        Manifest.permission.WRITE_EXTERNAL_STORAGE
    )
    private val INTENT_NAME = "INIT_MAP"

    private lateinit var map: Map
    private lateinit var mapFragment: SupportMapFragment

    private val musicians = arrayListOf<Musician>()

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        checkPermissions()
        findViewById<FloatingActionButton>(R.id.profileButton).setOnClickListener {
            val intent = Intent(this, MainMusician::class.java)
            startActivity(intent)
        }
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

                    Log.i("forEach", "Before")
                    val musiciansMarkers = ArrayList<MapMarker>()
                    Thread {
                        while(true) {
                            musiciansMarkers.clear()
                            map.removeMapObjects(musiciansMarkers.toList())

                            GrpcTask(musicians).execute()
                            musicians.forEach {
                                val image = Image()
                                image.bitmap = musicianIcon
                                musiciansMarkers.add(MapMarker(GeoCoordinate(it.xCoord, it.yCoord), image))
                                Log.i("ForEach", it.name)
                            }
                            map.addMapObjects(musiciansMarkers.toList())
                            val copyMusicians = musicians.toTypedArray()
                            if (copyMusicians.isNotEmpty())
                                this@MainActivity.runOnUiThread {
                                    findViewById<RecyclerView>(R.id.list).apply {
                                        adapter = MusicianAdapter(copyMusicians)
                                        layoutManager = LinearLayoutManager(this@MainActivity)
                                    }
                                }
                            Thread.sleep(10000)
                        }
                    }.start()

                } else {
                    Log.e("map.init", it.name)
                }
            }
        }
    }
}

private class GrpcTask constructor(_musicians: ArrayList<Musician>) : AsyncTask<Void, Void, String>() {
    private val musicians = _musicians
    private var channel: ManagedChannel? = null

    override fun doInBackground(vararg poof: Void) : String {
//            val host = "10.100.110.201"
        val host = "192.168.43.230"
        val port = 50051
        return try {
            channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()
            val stub = CommunicatorGrpc.newBlockingStub(channel)
            val request = EmptyMessage.newBuilder().build()
            val reply = stub.poll(request)
            Log.i("ForThread", "Before")
            musicians.clear()
            for (musician in reply) {
                musicians.add(musician)
                Log.i("ForThread", musician.name)
            }
            "OK"
        } catch (e: Exception) {
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            e.printStackTrace(pw)
            pw.flush()
            "Failed... : %s".format(sw)
        }
    }

    override fun onPostExecute(poof: String) {
        try {
            channel?.shutdown()?.awaitTermination(1, TimeUnit.SECONDS)
        } catch (e: InterruptedException) {
            Thread.currentThread().interrupt()
        }
    }
}
