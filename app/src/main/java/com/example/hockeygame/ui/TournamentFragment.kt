package com.example.hockeygame.ui

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.viewpager2.widget.ViewPager2
import com.example.hockeygame.R
import com.example.hockeygame.ui.tournament.adapters.TournamentPagerAdapter
import com.google.android.material.tabs.TabLayout
import com.google.android.material.tabs.TabLayoutMediator

class TournamentFragment : Fragment() {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        return inflater.inflate(R.layout.fragment_tournament, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)

        val tabLayout = view.findViewById<TabLayout>(R.id.tournaments_tabs)
        val viewPager = view.findViewById<ViewPager2>(R.id.tournaments_viewpager)

        val adapter = TournamentPagerAdapter(this)
        viewPager.adapter = adapter

        TabLayoutMediator(tabLayout, viewPager) { tab, position ->
            tab.text = when (position) {
                0 -> "Товарищеские"
                1 -> "Региональные"
                2 -> "ВХЛ"
                3 -> "НХЛ"
                else -> ""
            }
        }.attach()
    }
}