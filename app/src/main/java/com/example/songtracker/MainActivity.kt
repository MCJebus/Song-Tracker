package com.example.songtracker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuInflater
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.widget.TableLayout
import android.widget.TextView
import androidx.fragment.app.FragmentTransaction
import androidx.viewpager.widget.ViewPager
import com.google.android.material.tabs.TabLayout

class MainActivity : AppCompatActivity() {
    lateinit var db: DatabaseHelper

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Setup database
        db = DatabaseHelper(this)
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
        val temp: Lyric = Lyric(10, "Shes a maniac", "Heaven", "Buzz Lightyear", true)
        val temp2: Lyric = Lyric(10, "Maniac", "Heaven", "Buzz Lightyear", true)
        val temp3: Lyric = Lyric(10, "On the floor", "Heaven", "Buzz Lightyear", false)
        db.addLyric(temp)
        db.addLyric(temp2)
        db.addLyric(temp3)
    }
}
