package ru.goaliepash.playlistmaker.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.google.android.material.tabs.TabLayoutMediator
import ru.goaliepash.playlistmaker.R
import ru.goaliepash.playlistmaker.databinding.ActivityMediaLibraryBinding
import ru.goaliepash.playlistmaker.ui.adapter.MediaLibraryViewPagerAdapter

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaLibraryBinding
    private lateinit var tabLayoutMediator: TabLayoutMediator

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater).also { setContentView(it.root) }
        initUI()
    }

    private fun initUI() {
        initImageViewBack()
        initViewPagerAndTabLayout()
    }

    private fun initImageViewBack() {
        binding.imageViewBack.setOnClickListener { finish() }
    }

    private fun initViewPagerAndTabLayout() {
        binding.viewPager2.adapter = MediaLibraryViewPagerAdapter(supportFragmentManager, lifecycle)
        tabLayoutMediator = TabLayoutMediator(binding.tabLayout, binding.viewPager2) { tab, position ->
            when (position) {
                0 -> tab.text = getString(R.string.favorite_tracks)
                else -> tab.text = getString(R.string.playlists)
            }
        }
        tabLayoutMediator.attach()
    }

    override fun onDestroy() {
        super.onDestroy()
        tabLayoutMediator.detach()
    }
}