package ru.goaliepash.playlistmaker

import android.os.Bundle
import android.view.View
import android.widget.Button
import android.widget.Toast
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
            Toast
                .makeText(
                    this@MainActivity,
                    getString(R.string.search_button_click),
                    Toast.LENGTH_SHORT
                )
                .show()
        }
        searchButton.setOnClickListener(searchButtonClickListener)
    }

    private fun initMediaLibraryButton() {
        val mediaLibraryButton = findViewById<Button>(R.id.media_library_button)
        mediaLibraryButton.setOnClickListener {
            Toast
                .makeText(this, getString(R.string.media_library_button_click), Toast.LENGTH_SHORT)
                .show()
        }
    }

    private fun initSettingsButton() {
        val settingsButton = findViewById<Button>(R.id.settings_button)
        settingsButton.setOnClickListener {
            Toast
                .makeText(this, getString(R.string.settings_button_click), Toast.LENGTH_SHORT)
                .show()
        }
    }
}