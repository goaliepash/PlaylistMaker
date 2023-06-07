package ru.goaliepash.playlistmaker

import android.os.Bundle
import android.widget.ImageView
import androidx.appcompat.app.AppCompatActivity

class SettingsActivity : AppCompatActivity() {

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_settings)
        initUI()
    }

    private fun initUI() {
        initImageViewBack()
    }

    private fun initImageViewBack() {
        val imageViewBack = findViewById<ImageView>(R.id.imageViewBack)
        imageViewBack.setOnClickListener { finish() }
    }
}