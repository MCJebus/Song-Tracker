package com.example.songtracker

import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.TextView
import androidx.recyclerview.widget.RecyclerView
import com.google.android.material.snackbar.Snackbar

class SongRecyclerAdapter (private val songArrayList: MutableList<String?>) : RecyclerView.Adapter<SongRecyclerAdapter.ViewHolder>() {

    class ViewHolder(var layout: View) : RecyclerView.ViewHolder(layout) {
        var txtMsg: TextView

        init {
            txtMsg = layout.findViewById<View>(R.id.firstLine) as TextView
        }
    }

    override fun onCreateViewHolder(parent: ViewGroup, viewType: Int): ViewHolder {
        val inflater = LayoutInflater.from(parent.context)
        val v = inflater.inflate(R.layout.lyric_layout, parent, false)

        return ViewHolder(v)
    }

    override fun onBindViewHolder(holder: ViewHolder, position: Int) {
        val info = songArrayList[position]

        holder.txtMsg.setText(info)
        holder.txtMsg.setOnClickListener { v ->
            val snackbar = Snackbar.make(v, "You clicked on ${info}", Snackbar.LENGTH_LONG)
            snackbar.show()
        }
    }

    override fun getItemCount(): Int {
        return songArrayList.size
    }
}