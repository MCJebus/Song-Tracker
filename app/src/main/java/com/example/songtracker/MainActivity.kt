package com.example.songtracker

import android.content.Context
import android.content.Intent
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.view.MenuInflater
import android.view.Menu
import android.view.MenuItem
import androidx.viewpager.widget.ViewPager
import com.google.android.gms.maps.GoogleMap
import com.google.android.gms.maps.OnMapReadyCallback
import com.google.android.gms.maps.SupportMapFragment
import com.google.android.material.tabs.TabLayout
import java.io.OutputStreamWriter

class MainActivity : AppCompatActivity() {

    lateinit var mPreferences: SharedPreferences

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)

        //Get shared preferences.
        mPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)

        val editor = mPreferences.edit()
        //Check to see if app has been used before. Only puts preferences if re-installed.
        if (mPreferences.contains(ClassicSongNumber) && mPreferences.contains(ClassicLyricNumber) && mPreferences.contains(
                GameMode)) {

        } else {
            editor.putInt(ClassicSongNumber, 0)
            editor.putInt(ClassicLyricNumber, 3) //This should be changed to zero if not using preLoadSongData().
            editor.putString(GameMode, "Classic")
            editor.putInt(CurrentSongNumber, 0)
            editor.putInt(CurrentLyricNumber, 5)
        }
        editor.commit()

        //This function is used to preload 3 found lyrics for testing purposes.
        //preLoadSongData()

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
        var intent = Intent(this.applicationContext,SongsActivity::class.java)
        startActivity(intent)
    }

    //What happens if this menu option is selected.
    fun switchGameMode() {
        mPreferences = getSharedPreferences(sharedPrefFile, Context.MODE_PRIVATE)
        val editor = mPreferences.edit()
        if (mPreferences.getString(GameMode, "Classic").equals("Classic"))
        {
            editor.putString(GameMode, "Current")
        } else {
            editor.putString(GameMode, "Classic")
        }
        editor.commit()
    }

    //Used only for testing.
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
        //These numbers are used with the below arrays to determine what song the user is on.
        const val ClassicSongNumber = "classicSongNumberKey"
        const val ClassicLyricNumber = "classicLyricNumberKey"
        const val CurrentSongNumber = "currentSongNumberKey"
        const val CurrentLyricNumber = "currentLyricNumberKey"
        //This holds a string of the game mode either "Classic" or "Current"
        const val GameMode = "modeKey"

        //Holds all classic song filenames for reading lyrics.
        val classicSongs: Array<String> = arrayOf("bob_dylan(like_a_rolling_stone).txt", "david_bowie(life_on_mars_).txt",
            "elton_john(your_song).txt", "guns_n_roses(sweet_child_o_mine).txt","john_lennon(imagine).txt",
            "judy_garland(over_the_rainbow).txt", "led_zeppelin(stairway_to_heaven).txt", "michael_jackson(billie_jean).txt",
            "nirvana(smells_like_teen_spirit).txt", "oasis(live_forever).txt", "queen(bohemian_rhapsody).txt,",
            "rolling_stones(I_can't_get_no_satisfaction).txt", "sex_pistols(god_save_the_queen).txt", "the_beatles(hey_jude).txt",
            "the_clash(london_calling).txt", "the_eagles(hotel_california).txt", "the_kinks(waterloo_sunset).txt",
            "u2(one).txt", "whitney_houston(i_will_always_love_you).txt")

        //Holds all current song filenames for reading lyrics.
        val currentSongs: Array<String> = arrayOf("a_j_tracey(ladbroke_grove).txt", "aitch(taste_make_it_shake).txt",
            "ariana_grande_:_miley_cyrus_:_lana_del_rey(don't_call_me_angle).txt", "dave(professor_x).txt",
            "dermot_kennedy(outnumbered).txt", "dominic_fike(3_nights).txt", "ed_sheeran_ft_stormzy(take_me_back_to_london).txt",
            "headie_one(both).txt", "joel_corry(sorry).txt", "jorja_smith_ft_burna_boy(be_honest).txt", "kygo_&_whitney_houston(higher_love).txt",
            "lil_tecca(ransom).txt", "post_malone(circles).txt", "post_malone_ft_young_thug(goodbyes).txt", "regard(ride_it).txt",
            "sam_feldt_ft_rani(post_malone).txt", "sam_smith(how_do_you_sleep).txt", "shawn_mendes_:_camila_cabello(senorita).txt",
            "tones_&_i(dance_monkey).txt", "young_t_&_bugsey_ft_aitch(strike_a_pose).txt")

        //Holds all classic song hints for help.
        val classicHints: Array<String> = arrayOf("1965 it by american singer.", "This song came from another planet.",
            "Classic hit by British icon", "Band also write 'Paradise City'", "You'll have to ... what it would be like",
            "Somewheeeeeere", "One way to get to paradise", "Created by the 'King Of Pop'", "Bands lead killed himself",
            "Band made up of 2 brothers", "Controversial frontman recently had a film made about his life",
            "Band has been mentioned before in this app", "British Sexual Weaponry Band", "Possibly the biggest band of all time",
            "You hear the british capital shouting for you.", "Hotel in the golden state.", "Night time in waterloo",
            "The one", "Annnnnnnnd  IIIIIIIEEIII")
    }
}
