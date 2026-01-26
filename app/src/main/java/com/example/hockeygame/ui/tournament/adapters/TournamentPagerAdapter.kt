package com.example.hockeygame.ui.tournament.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hockeygame.ui.tournament.tabs.NHLFragment
import com.example.hockeygame.ui.tournament.tabs.UnrankedFragment
import com.example.hockeygame.ui.tournament.tabs.VHLFragment

class TournamentPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> UnrankedFragment()
            1 -> VHLFragment()
            2 -> NHLFragment()
            else -> Fragment()
        }
    }
}