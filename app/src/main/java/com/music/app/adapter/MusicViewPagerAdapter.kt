package com.music.app.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.music.app.fragment.ListSongPlayingFragment
import com.music.app.fragment.PlaySongFragment

class MusicViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return if (position == 0) {
            ListSongPlayingFragment()
        } else PlaySongFragment()
    }

    override fun getItemCount(): Int {
        return 2
    }
}