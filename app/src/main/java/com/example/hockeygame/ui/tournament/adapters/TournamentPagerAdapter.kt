package com.example.hockeygame.ui.tournament.adapters

import androidx.fragment.app.Fragment
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.example.hockeygame.ui.team.tabs.TeamLineupFragment
import com.example.hockeygame.ui.team.tabs.TeamStatsFragment

class TournamentPagerAdapter(fragment: Fragment) : FragmentStateAdapter(fragment) {

    override fun getItemCount(): Int = 3  // ✅ Изменено с 2 на 3

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> TeamLineupFragment()      // Состав
            1 -> TeamStatsFragment()       // Статистика команды
            2 -> Fragment()                // ✅ Добавлен третий фрагмент (пока пустой)
            else -> Fragment()
        }
    }
}