package com.example.hockeygame.ui.team.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hockeygame.ui.team.TeamLineupFragment
import com.example.hockeygame.ui.team.TeamStatsFragment

class TeamPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 2

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TeamLineupFragment()      // Состав
            1 -> TeamStatsFragment()      // Статистика команды
            else -> Fragment()
        }
    }
}