package ru.goaliepash.playlistmaker

import android.content.Intent
import android.os.Bundle
import android.view.View
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity

class MainActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
        initUI()
    }

    private fun initUI() {
        initSearchButton()
        initMediaLibraryButton()
        initSettingsButton()
    }

    private fun initSearchButton() {
        val searchButton = findViewById<Button>(R.id.search_button)
        val searchButtonClickListener = View.OnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        searchButton.setOnClickListener(searchButtonClickListener)
    }

    private fun initMediaLibraryButton() {
        val mediaLibraryButton = findViewById<Button>(R.id.media_library_button)
        mediaLibraryButton.setOnClickListener {
            val intent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initSettingsButton() {
        val settingsButton = findViewById<Button>(R.id.settings_button)
        settingsButton.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}