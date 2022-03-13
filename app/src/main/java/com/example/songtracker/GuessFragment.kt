package com.example.songtracker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.TextView
import androidx.appcompat.widget.AppCompatTextView
import androidx.fragment.app.Fragment
import com.example.songtracker.MainActivity.Companion.GameMode
import com.example.songtracker.MainActivity.Companion.ClassicLyricNumber
import com.example.songtracker.MainActivity.Companion.sharedPrefFile
import com.example.songtracker.MainActivity.Companion.ClassicSongNumber
import com.example.songtracker.MainActivity.Companion.CurrentLyricNumber
import com.example.songtracker.MainActivity.Companion.CurrentSongNumber
import com.example.songtracker.MainActivity.Companion.classicHints
import com.example.songtracker.MainActivity.Companion.classicSongs
import com.example.songtracker.MainActivity.Companion.currentSongs
import com.google.android.material.snackbar.Snackbar
import kotlinx.android.synthetic.main.guess_fragment.view.*
import java.io.OutputStreamWriter

class GuessFragment : Fragment(), View.OnClickListener {

    lateinit var mPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val vi = inflater!!.inflate(R.layout.guess_fragment, container, false)
        //Setup on click listeners for buttons.
        val submit = vi.findViewById(R.id.button_submit) as Button
        val hint = vi.findViewById(R.id.button_hint) as Button
        val skip = vi.findViewById(R.id.button_skip) as Button
        submit.setOnClickListener(this)
        hint.setOnClickListener(this)
        skip.setOnClickListener(this)

        return vi
    }

    companion object {
        fun newInstance(): GuessFragment = GuessFragment()
    }

    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_submit -> {
                //Checks answers based on game modes.
                mPreferences = getActivity()!!.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
                if (mPreferences.getString(GameMode, "Classic").equals("Classic")) {
                    checkClassicAnswers(v)
                } else {
                    checkCurrentAnswers(v)
                }
            }
            R.id.button_hint -> {
                //Displays hints.
                mPreferences = getActivity()!!.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
                val song = mPreferences.getInt(ClassicSongNumber, 0)
                val hintText = getActivity()!!.findViewById(R.id.text_hint) as AppCompatTextView
                hintText.setText(classicHints[song])
            }
            R.id.button_skip -> {
                //Ski song based on game mode.
                mPreferences = getActivity()!!.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
                val editor = mPreferences.edit()
                if (mPreferences.getString(GameMode, "Classic").equals("Classic")) {
                    val number = mPreferences.getInt(ClassicSongNumber, 0) + 1
                    editor.putInt(ClassicSongNumber, number)
                    editor.putInt(ClassicLyricNumber, 0)
                } else {
                    val number = mPreferences.getInt(CurrentSongNumber, 0) + 1
                    editor.putInt(CurrentSongNumber, number)
                    editor.putInt(CurrentLyricNumber, 0)
                }
                editor.commit()
                val snackbar = Snackbar.make(v, "You skipped a song.", Snackbar.LENGTH_LONG)
                snackbar.show()
            }
            else -> {

            }
        }
    }
    fun checkClassicAnswers(v: View) {
        val song = view!!.song_text_input.text.toString()
        val artist = view!!.artist_text.text.toString()
        val answer: String = artist + "(" + song + ").txt"
        mPreferences = getActivity()!!.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        if (mPreferences.contains(ClassicSongNumber)) {
            var prefanswer = classicSongs[mPreferences.getInt(ClassicSongNumber, 0)]
            if (prefanswer == answer) {
                changeClassicSong()
                val snackbar = Snackbar.make(v, "Correct " + mPreferences.getInt(ClassicSongNumber, 1).toString(), Snackbar.LENGTH_LONG)
                snackbar.show()
            } else {
                val snackbar = Snackbar.make(v, "Incorrect " + classicSongs[mPreferences.getInt(ClassicSongNumber, 0)], Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        } else {
            val snackbar = Snackbar.make(v, "Preferences could not be found", Snackbar.LENGTH_LONG)
            snackbar.show()
        }
    }

    fun checkCurrentAnswers(v: View) {
        val song = view!!.song_text_input.text.toString()
        val artist = view!!.artist_text.text.toString()
        val answer: String = artist + "(" + song + ").txt"
        mPreferences = getActivity()!!.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        if (mPreferences.contains(CurrentSongNumber)) {
            var prefanswer = currentSongs[mPreferences.getInt(CurrentSongNumber, 0)]
            if (prefanswer == answer) {
                changeCurrentSong()
                val snackbar = Snackbar.make(v, "Correct " + mPreferences.getInt(CurrentSongNumber, 1).toString(), Snackbar.LENGTH_LONG)
                snackbar.show()
            } else {
                val snackbar = Snackbar.make(v, "Incorrect " + currentSongs[mPreferences.getInt(CurrentSongNumber, 0)], Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        } else {
            val snackbar = Snackbar.make(v, "Preferences could not be found", Snackbar.LENGTH_LONG)
            snackbar.show()
        }
    }

    fun changeClassicSong() {
        mPreferences = getActivity()!!.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        var prefnumber = mPreferences.getInt(ClassicSongNumber, 0) + 1
        val editor = mPreferences.edit()
        editor.putInt(ClassicSongNumber, prefnumber)
        editor.putInt(ClassicLyricNumber, 0) //When changed to new song there should be no lyrics found.
        editor.commit()
        clearLyrics()
    }

    fun changeCurrentSong() {
        mPreferences = getActivity()!!.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        var prefnumber = mPreferences.getInt(CurrentSongNumber, 0) + 1
        val editor = mPreferences.edit()
        editor.putInt(CurrentSongNumber, prefnumber)
        editor.putInt(CurrentLyricNumber, 0) //When changed to new song there should be no lyrics found.
        editor.commit()
        clearLyrics()
    }

    fun clearLyrics() {
        try {
            val fileout = getActivity()!!.openFileOutput("lyrics.txt", Context.MODE_PRIVATE)
            val outputWriter = OutputStreamWriter(fileout)
            outputWriter.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }
}