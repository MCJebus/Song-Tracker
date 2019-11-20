package com.example.songtracker

import android.content.Context
import android.content.SharedPreferences
import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import java.io.BufferedReader
import java.io.File
import java.io.FileInputStream
import java.io.InputStreamReader
import java.io.OutputStreamWriter

class LyricFragment() : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.lyric_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lyricModelArrayList: ArrayList<String?> = populateList()

        //2. set layoutManager
        val recyclerView = view.findViewById<View>(R.id.my_recycler_view) as RecyclerView
        val layoutManager = LinearLayoutManager(getActivity())
        recyclerView.layoutManager = layoutManager
        //3. Create adapter
        val mAdapter = LyricRecyclerAdapter(lyricModelArrayList)
        //4. Set Adapter
        recyclerView.adapter = mAdapter
    }

    fun readFileAsLinesUsingBufferedReader(fileName: String): List<String>
            = activity!!.applicationContext.assets.open(fileName).bufferedReader().readLines()

    private fun populateList(): ArrayList<String?> {
        val list2 = ArrayList<String?>()
        try {
            var fileInputStream: FileInputStream? = null
            fileInputStream = activity?.openFileInput("lyrics.txt")
            var inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var text: String? = null
            while ({ text = bufferedReader.readLine(); text }() != null) {
                list2.add(text)
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }

        return list2
    }

    companion object {
        fun newInstance(): LyricFragment = LyricFragment()
    }

}