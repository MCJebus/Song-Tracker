package com.example.songtracker

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentPagerAdapter
import androidx.fragment.app.FragmentStatePagerAdapter

class FragmentAdapter(fm: FragmentManager) : FragmentStatePagerAdapter(fm) {

    override fun getItem(position: Int): Fragment {
        val fragment : Fragment
        when (position) {
            0 -> fragment = GuessFragment.newInstance()
            1 -> fragment = MapFragment.newInstance()
            2 -> fragment = LyricFragment.newInstance()
            else -> fragment = GuessFragment.newInstance()
        }
        return fragment
    }


    override fun getPageTitle(position: Int): CharSequence = when (position) {
        0 -> "Guess"
        1 -> "Map"
        2 -> "Lyrics"
        else -> ""
    }

    override fun getCount(): Int = 3
}