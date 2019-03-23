    package ru.ifmo.se.musician
import android.support.v7.widget.RecyclerView
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import ru.ifmo.se.protofiles.Musician

class MusicianAdapter(private val list: Array<Musician>): RecyclerView.Adapter<MusicianAdapter.MusicianHolder>() {

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): MusicianHolder {
        val view = LayoutInflater.from(parent.context).inflate(R.layout.list_row, parent, false)
        return MusicianHolder(view)
    }

    override fun getItemCount(): Int {
        return list.size
    }

    override fun onBindViewHolder(holder: MusicianHolder, i: Int) {
        val musician = list[i]
        holder.name.text = musician.name
        holder.types.text = musician.generesList.joinToString()
        holder.startTime.text = musician.startTime
        holder.endTime.text = musician.endTime
    }

    class MusicianHolder(view: View): RecyclerView.ViewHolder(view){
        val name = view.findViewById<TextView>(R.id.musicianName)
        val types = view.findViewById<TextView>(R.id.musicianTypes)
        val startTime = view.findViewById<TextView>(R.id.startTime)
        val endTime = view.findViewById<TextView>(R.id.endTime)
    }
}