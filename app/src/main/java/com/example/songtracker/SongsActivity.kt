package com.example.songtracker

import android.app.PendingIntent.getActivity
import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.PersistableBundle
import android.util.AttributeSet
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.songtracker.MainActivity.Companion.ClassicSongNumber
import com.example.songtracker.MainActivity.Companion.CurrentSongNumber
import com.example.songtracker.MainActivity.Companion.classicSongs
import com.example.songtracker.MainActivity.Companion.currentSongs
import com.example.songtracker.MainActivity.Companion.sharedPrefFile
import kotlinx.android.synthetic.main.activity_songs.*

class SongsActivity : AppCompatActivity() {

    lateinit var mPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_songs)

        var songArrayList: ArrayList<String?> = getSolvedSongsList()
        //2. set layoutManager
        val recyclerView = findViewById<View>(R.id.song_recycler_view) as RecyclerView
        val layoutManager = LinearLayoutManager(this)
        recyclerView.layoutManager = layoutManager
        //3. Create adapter
        val mAdapter = SongRecyclerAdapter(songArrayList)
        //4. Set Adapter
        recyclerView.adapter = mAdapter
    }

    //Based on shared preferences.
    fun getSolvedSongsList(): ArrayList<String?> {
        mPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        var list = ArrayList<String?>()
        var count = 0
        while (count < mPreferences.getInt(ClassicSongNumber, 0)) {
            list.add(classicSongs[count])
            count+=1
        }
        count = 0
        while (count < mPreferences.getInt(CurrentSongNumber, 0)) {
            list.add(currentSongs[count])
            count+=1
        }
        return list

    }
}
