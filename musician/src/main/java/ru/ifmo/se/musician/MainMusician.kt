package ru.ifmo.se.musician

import android.os.AsyncTask
import android.os.Bundle
import android.support.design.widget.TextInputEditText
import android.support.v7.app.AppCompatActivity
import android.util.Log
import android.widget.Button
import android.widget.EditText
import android.widget.TextView
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

        findViewById<Button>(R.id.addProfile).setOnClickListener {
            val address = findViewById<TextInputEditText>(R.id.location).text.toString()
            val startTime = findViewById<TextInputEditText>(R.id.start_time).text.toString()
            val endTime = findViewById<TextInputEditText>(R.id.end_time).text.toString()
            val builder = Musician.newBuilder().setStartTime(startTime).setEndTime(endTime)

            val request = GeocodeRequest2(address).setSearchArea(GeoCoordinate(59.9343, 30.3351), 10)
            request.execute(MyResultListener(builder))
        }
    }

    class MyResultListener(val builder: Musician.Builder): ResultListener<MutableList<GeocodeResult>>{

        override fun onCompleted(data: MutableList<GeocodeResult>, error: ErrorCode?) {
            if (error == ErrorCode.NONE && data.size != 0){
                val coordinate = data[0].location.coordinate
                builder.apply {
                    xCoord = coordinate.latitude
                    yCoord = coordinate.longitude
                    name = "Lol"
                }
                val success = false
                while(!success){
                    PushMusician(builder.build(), success).execute()
                    Thread.sleep(1000)
                }
            }
        }

    }
}

private class PushMusician constructor(val musician: Musician, var success: Boolean) : AsyncTask<Void, Void, String>() {
    private var channel: ManagedChannel? = null

    override fun doInBackground(vararg poof: Void): String {
            val host = "10.100.110.201"
//        val host = "192.168.43.230"
        val port = 50051
        return try {
            channel = ManagedChannelBuilder.forAddress(host, port).usePlaintext().build()
            val stub = CommunicatorGrpc.newBlockingStub(channel)
            val request = musician
            stub.send(request)
            success = true
            "OK"
        } catch (e: Exception) {
            val sw = StringWriter()
            val pw = PrintWriter(sw)
            e.printStackTrace(pw)
            pw.flush()
            Log.i("error", e.message)
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
