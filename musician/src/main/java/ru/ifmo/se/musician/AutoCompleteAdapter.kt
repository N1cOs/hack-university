package ru.ifmo.se.musician

import android.content.Context
import android.view.View
import android.view.ViewGroup
import android.widget.Adapter
import android.widget.ArrayAdapter

class AutoCompleteAdapter(context1: Context, id: Int, list: MutableList<String>):
    ArrayAdapter<String>(context1, id, list) {

//    override fun getView(position: Int, convertView: View?, parent: ViewGroup): View {
//        return parent.findViewById(R.id.adresses)
//    }
}