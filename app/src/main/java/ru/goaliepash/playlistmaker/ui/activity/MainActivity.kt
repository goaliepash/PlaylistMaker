package ru.goaliepash.playlistmaker.ui.activity

import android.content.Intent
import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import ru.goaliepash.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        initUI()
    }

    private fun initUI() {
        initButtonSearch()
        initButtonMediaLibrary()
        initButtonSettings()
    }

    private fun initButtonSearch() {
        val buttonSearchClickListener = View.OnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        binding.buttonSearch.setOnClickListener(buttonSearchClickListener)
    }

    private fun initButtonMediaLibrary() {
        binding.buttonMediaLibrary.setOnClickListener {
            val intent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initButtonSettings() {
        binding.buttonSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}