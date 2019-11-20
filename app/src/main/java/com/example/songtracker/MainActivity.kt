package com.example.songtracker

import android.content.Context
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.EditText
import android.widget.TableLayout
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {
    lateinit var db: DatabaseHelper
    lateinit var mPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Setup database
        db = DatabaseHelper(this)
        //Get shared preferences.
        mPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        val editor = mPreferences.edit()
        //Check to see if app has been used before. Only puts string if re-installed.
        if (mPreferences.contains(Number)) {

        } else {
            editor.putInt(Number, 0)
        }
        editor.commit()

        preLoadSongData()

        //Setup Tab Layout and Action Bar
        setSupportActionBar(findViewById(R.id.toolbar))

        val tabLayout: TabLayout = findViewById(R.id.tab_layout)

        val viewPager: ViewPager = findViewById(R.id.view_pager)

        val adapter = FragmentAdapter(supportFragmentManager)

        viewPager.adapter = adapter
        tabLayout.setupWithViewPager(viewPager)

        tabLayout.addOnTabSelectedListener(object : TabLayout.OnTabSelectedListener {
            override fun onTabSelected(tab: TabLayout.Tab) {

            }

            override fun onTabUnselected(tab: TabLayout.Tab) {

            }

            override fun onTabReselected(tab: TabLayout.Tab) {

            }
        })

    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        val inflater: MenuInflater = menuInflater
        inflater.inflate(R.menu.toolbar_menu, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle item selection
        return when (item.itemId) {
            R.id.solved_songs -> {
                solvedSongs()
                true
            }
            R.id.switch_mode -> {
                switchGameMode()
                true
            }
            else -> super.onOptionsItemSelected(item)
        }
    }

    //What happens if this menu option is selected.
    fun solvedSongs() {

    }

    //What happens if this menu option is selected.
    fun switchGameMode() {

    }

    fun preLoadSongData() {
        try {
            val fileout = openFileOutput("lyrics.txt", Context.MODE_PRIVATE)
            val outputWriter = OutputStreamWriter(fileout)
            outputWriter.write("Shes a maniac")
            outputWriter.append("\n")
            outputWriter.write("on the floor")
            outputWriter.close()
        } catch (e: Exception) {
            e.printStackTrace()
        }
    }

    companion object {
        const val sharedPrefFile = "mypref"
        const val Number = "numberKey"
        val songs: Array<String> = arrayOf("bob_dylan(like_a_rolling_stone).txt", "david_bowie(life_on_mars).txt",
            "elton_john(your_song).txt", "guns_n_roses(sweet_child_o_mine).txt","john_lennon(imagine).txt",
            "judy_garland(over_the_rainbow).txt", "led_zeppelin(stairway_to_heaven).txt", "michael_jackson(billie_jean).txt",
            "nirvana(smells_like_teen_spirit).txt", "oasis(live_forever).txt", "queen(bohemian_rhapsody).txt,",
            "rolling_stones(I_can't_get_no_satisfaction).txt", "sex_pistols(god_save_the_queen).txt", "the_beatles(hey_jude).txt",
            "the_clash(london_calling).txt", "the_eagles(hotel_california).txt", "the_kinks(waterloo_sunset).txt",
            "u2(one).txt", "whitney_houston(i_will_always_love_you).txt")
    }
}
