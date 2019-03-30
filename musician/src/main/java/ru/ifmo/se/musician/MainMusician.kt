package ru.ifmo.se.musician

import android.content.pm.ActivityInfo
import android.graphics.Color.argb
import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v4.content.res.ResourcesCompat
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.*
import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.search.*
import io.grpc.Grpc
import io.grpc.ManagedChannel
import io.grpc.ManagedChannelBuilder
import ru.ifmo.se.protofiles.CommunicatorGrpc
import ru.ifmo.se.protofiles.EmptyMessage
import ru.ifmo.se.protofiles.Musician
import java.io.PrintWriter
import java.io.StringWriter
import java.util.concurrent.TimeUnit

class MainMusician : AppCompatActivity() {


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main_musician)
        requestedOrientation = ActivityInfo.SCREEN_ORIENTATION_PORTRAIT

        findViewById<Button>(R.id.addProfile).setOnClickListener {
            val address = findViewById<TextInputEditText>(R.id.location).text.toString()
            val startTime = findViewById<TextInputEditText>(R.id.start_time).text.toString()
            val endTime = findViewById<TextInputEditText>(R.id.end_time).text.toString()
            val builder = Musician.newBuilder().setStartTime(startTime).setEndTime(endTime)

            val request = GeocodeRequest2(address).setSearchArea(GeoCoordinate(59.9343, 30.3351), 10)
            request.execute(MyResultListener(builder))
            finish()
        }


//        findViewById<Button>(R.id.add_sing_button).setOnClickListener {
//
//            val signArea = findViewById<TextInputEditText>(R.id.sing_appender).text.toString()
//            if (!signArea.equals("")) {
//                val address = findViewById<ListView>(R.id.traks)
//
//                val textView = TextView(this)
//                textView.layoutParams = LinearLayout.LayoutParams(
//                    LinearLayout.LayoutParams.MATCH_PARENT,
//                    LinearLayout.LayoutParams.WRAP_CONTENT)
//                textView.text = signArea
//                textView.setTextColor(argb(0xff, 0x00, 0x00, 0x00))
//                textView.setPadding(58, 4, 4, 4)
//
//                address.addView(textView)
//
//            }
//
//        }

        findViewById<Button>(R.id.back_button).setOnClickListener {
            finish()
        }
    }

    class MyResultListener(val builder: Musician.Builder): ResultListener<MutableList<GeocodeResult>>{

        override fun onCompleted(data: MutableList<GeocodeResult>, error: ErrorCode?) {
            if (error == ErrorCode.NONE && data.size != 0){
                val coordinate = data[0].location.coordinate
                builder.apply {
                    xCoord = coordinate.latitude
                    yCoord = coordinate.longitude
                    name = "You"
                }
                val success = ObjectBoolean(false)
                while(!success.success){
                    PushMusician(builder.build(), success).execute()
                    Thread.sleep(1000)
                }
            }
        }

    }
}

private class ObjectBoolean(var success: Boolean)

private class PushMusician constructor(val musician: Musician, var success: ObjectBoolean) : AsyncTask<Void, Void, String>() {
    private var channel: ManagedChannel? = null

    override fun doInBackground(vararg poof: Void): String {
//            val host = "10.0.7.223"
//        val host = "192.168.43.230"
        val host = "35.228.95.2"
        val port = 50051
        return try {
            Log.i("ForBuiled", "HERE")
            channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()
            val stub = CommunicatorGrpc.newBlockingStub(channel)
            val request = musician
            Log.i("ForBuiled", "Sending")
            stub.send(request)
            Log.i("ForBuiled", "Sended")
            success.success = true
            "OK"
        } catch (e: Exception) {
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            e.printStackTrace(pw)
            pw.flush()
            Log.i("Forerror", e.message)
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
