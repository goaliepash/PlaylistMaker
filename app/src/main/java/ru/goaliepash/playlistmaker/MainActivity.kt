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
        initButtonSearch()
        initButtonMediaLibrary()
        initButtonSettings()
    }

    private fun initButtonSearch() {
        val buttonSearch = findViewById<Button>(R.id.button_search)
        val buttonSearchClickListener = View.OnClickListener {
            val intent = Intent(this, SearchActivity::class.java)
            startActivity(intent)
        }
        buttonSearch.setOnClickListener(buttonSearchClickListener)
    }

    private fun initButtonMediaLibrary() {
        val buttonMediaLibrary = findViewById<Button>(R.id.button_media_library)
        buttonMediaLibrary.setOnClickListener {
            val intent = Intent(this, MediaLibraryActivity::class.java)
            startActivity(intent)
        }
    }

    private fun initButtonSettings() {
        val buttonSettings = findViewById<Button>(R.id.button_settings)
        buttonSettings.setOnClickListener {
            val intent = Intent(this, SettingsActivity::class.java)
            startActivity(intent)
        }
    }
}