package com.practicum.playlistmaker.ui.media.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import com.practicum.playlistmaker.ui.favorites.FavoritesFragment
import com.practicum.playlistmaker.ui.playlists.PlaylistsFragment

class MediaLibraryViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) :
    FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            1 -> PlaylistsFragment.newInstance()
            else -> FavoritesFragment.newInstance()
        }
    }
}