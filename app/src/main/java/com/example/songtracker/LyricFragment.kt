package com.example.songtracker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.songtracker.MainActivity.Companion.ClassicLyricNumber
import com.example.songtracker.MainActivity.Companion.ClassicSongNumber
import com.example.songtracker.MainActivity.Companion.CurrentLyricNumber
import com.example.songtracker.MainActivity.Companion.CurrentSongNumber
import com.example.songtracker.MainActivity.Companion.GameMode
import com.example.songtracker.MainActivity.Companion.classicSongs
import com.example.songtracker.MainActivity.Companion.currentSongs
import java.io.*

class LyricFragment() : Fragment() {

    lateinit var mPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
    }
    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? =
        inflater.inflate(R.layout.lyric_fragment, container, false)

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        var lyricModelArrayList: ArrayList<String?>
        mPreferences = getActivity()!!.getSharedPreferences(MainActivity.sharedPrefFile, Context.MODE_PRIVATE)
        if (mPreferences.getString(GameMode, "Classic").equals("Classic"))
        {
            lyricModelArrayList = getClassicLyricsList()
        } else {
            lyricModelArrayList = getCurrentLyricsList()
        }
        //2. set layoutManager
        val recyclerView = view.findViewById<View>(R.id.my_recycler_view) as RecyclerView
        val layoutManager = LinearLayoutManager(getActivity())
        recyclerView.layoutManager = layoutManager
        //3. Create adapter
        val mAdapter = LyricRecyclerAdapter(lyricModelArrayList)
        //4. Set Adapter
        recyclerView.adapter = mAdapter
    }

    //Function for testing purposes.
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
            inputStreamReader.close()
        } catch (e: Exception) {
            e.printStackTrace()
        } finally {
        }

        return list2
    }

    fun getClassicLyricsList(): ArrayList<String?> {
        mPreferences = getActivity()!!.getSharedPreferences(MainActivity.sharedPrefFile, Context.MODE_PRIVATE)
        val list = ArrayList<String?>()
        var actualList = ArrayList<String?>()
        try {
            var fileInputStream: InputStream? = null
            var filepath: String = classicSongs[mPreferences.getInt(ClassicSongNumber, 1)]
            fileInputStream = getActivity()!!.getAssets().open(filepath)
            var inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var text: String? = null
            var count: Int = 0
            var lyricNumber = mPreferences.getInt(ClassicLyricNumber, 0)
            while (({ text = bufferedReader.readLine(); text }() != null)) {
                    list.add(text)
            }
            while (count < mPreferences.getInt(ClassicLyricNumber, 0)) {
                actualList.add(list[count])
                count+=1
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


        return actualList

    }

    fun getCurrentLyricsList(): ArrayList<String?> {
        mPreferences = getActivity()!!.getSharedPreferences(MainActivity.sharedPrefFile, Context.MODE_PRIVATE)
        val list = ArrayList<String?>()
        var actualList = ArrayList<String?>()
        try {
            var fileInputStream: InputStream? = null
            var filepath: String = currentSongs[mPreferences.getInt(CurrentSongNumber, 1)]
            fileInputStream = getActivity()!!.getAssets().open(filepath)
            var inputStreamReader = InputStreamReader(fileInputStream)
            val bufferedReader = BufferedReader(inputStreamReader)
            var text: String? = null
            var count: Int = 0
            var lyricNumber = mPreferences.getInt(CurrentLyricNumber, 0)
            while (({ text = bufferedReader.readLine(); text }() != null)) {
                list.add(text)
            }
            while (count < mPreferences.getInt(CurrentLyricNumber, 0)) {
                actualList.add(list[count])
                count+=1
            }
        } catch (e: Exception) {
            e.printStackTrace()
        }


        return actualList

    }

    companion object {
        fun newInstance(): LyricFragment = LyricFragment()
    }

}