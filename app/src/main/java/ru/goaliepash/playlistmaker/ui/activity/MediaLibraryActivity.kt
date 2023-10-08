package ru.goaliepash.playlistmaker.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.goaliepash.playlistmaker.databinding.ActivityMediaLibraryBinding

class MediaLibraryActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMediaLibraryBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMediaLibraryBinding.inflate(layoutInflater).also { setContentView(it.root) }
    }
}