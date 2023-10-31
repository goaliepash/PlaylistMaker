package ru.goaliepash.playlistmaker.ui.adapter

import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.lifecycle.Lifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import ru.goaliepash.playlistmaker.ui.fragment.implementation.FavoriteTracksFragment
import ru.goaliepash.playlistmaker.ui.fragment.implementation.PlaylistsFragment

class MediaLibraryViewPagerAdapter(fragmentManager: FragmentManager, lifecycle: Lifecycle) : FragmentStateAdapter(fragmentManager, lifecycle) {

    override fun getItemCount(): Int {
        return 2
    }

    override fun createFragment(position: Int): Fragment {
        return when (position) {
            0 -> FavoriteTracksFragment.newInstance()
            else -> PlaylistsFragment.newInstance()
        }
    }
}