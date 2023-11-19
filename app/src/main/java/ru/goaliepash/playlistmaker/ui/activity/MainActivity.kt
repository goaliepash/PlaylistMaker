package ru.goaliepash.playlistmaker.ui.activity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import ru.goaliepash.playlistmaker.R
import ru.goaliepash.playlistmaker.databinding.ActivityMainBinding
import ru.goaliepash.playlistmaker.ui.fragment.implementation.MainFragment

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { setContentView(it.root) }
        if (savedInstanceState == null) {
            supportFragmentManager
                .beginTransaction()
                .add(R.id.fragment_container_view, MainFragment())
                .commit()
        }
    }
}