package com.example.hockeygame.ui.club.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hockeygame.ui.club.tabs.ManageFragment
import com.example.hockeygame.ui.club.tabs.NewsFragment

class ClubPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> NewsFragment()
            1 -> ManageFragment()
            else -> Fragment()
        }
    }
}