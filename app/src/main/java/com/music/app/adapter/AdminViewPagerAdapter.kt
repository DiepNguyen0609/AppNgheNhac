package com.music.app.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentActivity
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.music.app.fragment.admin.AdminAccountFragment
import com.music.app.fragment.admin.AdminArtistFragment
import com.music.app.fragment.admin.AdminCategoryFragment
import com.music.app.fragment.admin.AdminFeedbackFragment
import com.music.app.fragment.admin.AdminSongFragment

class AdminViewPagerAdapter(fragmentActivity: FragmentActivity) : FragmentStateAdapter(fragmentActivity) {
    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> AdminArtistFragment()
            2 -> AdminSongFragment()
            3 -> AdminFeedbackFragment()
            4 -> AdminAccountFragment()
            else -> AdminCategoryFragment()
        }
    }

    override fun getItemCount(): Int {
        return 5
    }
}