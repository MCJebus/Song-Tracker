package com.example.songtracker

import android.database.Cursor
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView

class LyricFragment() : Fragment() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.lyric_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        val lyricModelArrayList: ArrayList<Lyric> = populateList()

        //2. set layoutManager
        val recyclerView = view.findViewById<View>(R.id.my_recycler_view) as RecyclerView
        val layoutManager = LinearLayoutManager(getActivity())
        recyclerView.layoutManager = layoutManager
        //3. Create adapter
        val mAdapter = LyricRecyclerAdapter(lyricModelArrayList)
        //4. Set Adapter
        recyclerView.adapter = mAdapter
    }

    private fun populateList(): ArrayList<Lyric> {
        var db: DatabaseHelper = DatabaseHelper(getActivity())

        var res: Cursor = db.getFoundLyrics()

        val list2 = ArrayList<Lyric>()

        if (res.count == 0) {

        } else {
            while (res.moveToNext()) {
                var temp: Lyric = Lyric(res.getString(0).toInt(), res.getString(1), res.getString(2),res.getString(3), res.getString(4).toBoolean())
                list2.add(temp)
            }
        }

        val list = ArrayList<Lyric>()
        val myLyricList = arrayOf("Ooh, ooh, ooh", "Ooh, ooh", "Somewhere over the rainbow", "Way up high", "And the dreams that you dream of", "Once in a lullaby", "Somewhere over the rainbow", "Bluebirds fly")
        var pos = 1
        for (lyric in myLyricList) {
            val lyricModel = Lyric(pos, lyric, "Over the Rainbow", "Judy Garland", true)
            list.add(lyricModel)
            pos =+ 1
        }
        return list2
    }

    companion object {
        fun newInstance(): LyricFragment = LyricFragment()
    }

}