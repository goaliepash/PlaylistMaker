package ru.goaliepash.playlistmaker.ui.fragment.implementation

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.navigation.fragment.findNavController
import com.google.android.material.tabs.TabLayoutMediator
import ru.goaliepash.playlistmaker.R
import ru.goaliepash.playlistmaker.databinding.FragmentMediaLibraryBinding
import ru.goaliepash.playlistmaker.ui.adapter.MediaLibraryViewPagerAdapter
import ru.goaliepash.playlistmaker.ui.fragment.BindingFragment

class MediaLibraryFragment : BindingFragment<FragmentMediaLibraryBinding>() {

    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun createBinding(inflater: LayoutInflater, container: ViewGroup?): FragmentMediaLibraryBinding {
        return FragmentMediaLibraryBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        initUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
    }

    private fun initUI() {
        initImageViewBack()
        initViewPagerAndTabLayout()
    }

    private fun initImageViewBack() {
        binding.imageViewBack.setOnClickListener {
            findNavController().navigateUp()
        }
    }

    private fun initViewPagerAndTabLayout() {
        binding.viewPager2.adapter = MediaLibraryViewPagerAdapter(childFragmentManager, lifecycle)
        tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                else -> tab.text = getString(R.string.playlists)
            }
        }
        tabLayoutMediator.attach()
    }
}