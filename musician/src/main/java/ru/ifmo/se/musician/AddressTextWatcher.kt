package ru.ifmo.se.musician

import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import com.here.android.mpa.common.GeoCoordinate
import com.here.android.mpa.search.*

class AddressTextWatcher(val adaptersValues: MutableList<String>): TextWatcher, ResultListener<MutableList<GeocodeResult>> {

    private lateinit var addreses: MutableList<String>

    override fun afterTextChanged(s: Editable?) {
        addreses.forEach {
            adaptersValues.add(it)
        }
    }

    override fun beforeTextChanged(s: CharSequence, start: Int, count: Int, after: Int) {

    }

    override fun onTextChanged(s: CharSequence, start: Int, before: Int, count: Int) {
        Log.i("autoComplete", "before text changed")
        addreses = ArrayList()

        if(!s.isEmpty()){
            val request = GeocodeRequest2(s.toString()).setSearchArea(GeoCoordinate(59.9343, 30.3351), 50)
            request.execute(this)
        }
    }

    override fun onCompleted(data: MutableList<GeocodeResult>, error: ErrorCode?) {
        if (error == ErrorCode.NONE){
            data.forEach {
                addreses.add(it.location.address.street)
            }
        }
    }
}