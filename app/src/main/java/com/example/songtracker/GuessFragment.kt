package com.example.songtracker

import android.content.Context
import android.content.SharedPreferences
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.EditText
import androidx.fragment.app.Fragment
import com.example.songtracker.MainActivity.Companion.sharedPrefFile
import com.example.songtracker.MainActivity.Companion.Song
import com.example.songtracker.MainActivity.Companion.Artist
import com.google.android.material.snackbar.Snackbar
import com.google.android.material.textfield.TextInputEditText
import com.google.android.material.textfield.TextInputLayout
import kotlinx.android.synthetic.main.guess_fragment.view.*

class GuessFragment : Fragment(), View.OnClickListener {

    lateinit var mPreferences: SharedPreferences

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?,
                              savedInstanceState: Bundle?): View? {
        val vi = inflater!!.inflate(R.layout.guess_fragment, container, false)
        val submit = vi.findViewById(R.id.button_submit) as Button
        submit.setOnClickListener(this)

        return vi
    }

    companion object {
        fun newInstance(): GuessFragment = GuessFragment()
    }

    fun onSubmitButtonClick(view: View) {

    }
    override fun onClick(v: View) {
        when (v.id) {
            R.id.button_submit -> {
                checkAnswers(v)
            }
            else -> {

            }
        }
    }
    fun checkAnswers(v: View) {
        val song = view!!.song_text_input.text.toString()
        val artist = view!!.artist_text.text.toString()
        mPreferences = getActivity()!!.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        if (mPreferences.contains(Song) && mPreferences.contains(Artist)) {
            var prefsong = mPreferences.getString(Song, "default")
            var prefartist = mPreferences.getString(Artist, "default")
            if (prefsong == song && prefartist == artist) {
                val snackbar = Snackbar.make(v, "Correct", Snackbar.LENGTH_LONG)
                snackbar.show()
                changeCurrentSong()
            } else {
                val snackbar = Snackbar.make(v, "Incorrect", Snackbar.LENGTH_LONG)
                snackbar.show()
            }
        } else {
            val snackbar = Snackbar.make(v, "Preferences could not be found", Snackbar.LENGTH_LONG)
            snackbar.show()
        }
    }
    fun changeCurrentSong() {
        mPreferences = getActivity()!!.getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        val editor = mPreferences.edit()
        editor.putString(Song, "life_on_mars")
        editor.putString(Artist, "david_bowie")
        editor.commit()
    }
}