package ru.goaliepash.playlistmaker.ui.activity

import android.os.Bundle
import android.view.View
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.ui.setupWithNavController
import ru.goaliepash.playlistmaker.R
import ru.goaliepash.playlistmaker.databinding.ActivityMainBinding

class MainActivity : AppCompatActivity() {

    private lateinit var binding: ActivityMainBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also {
            setContentView(it.root)
        }
        val navHostFragment =
            supportFragmentManager.findFragmentById(R.id.fragment_container_view) as NavHostFragment
        val navController = navHostFragment.navController
        setupNavController(navController)
        binding.bottomNavigationView.setupWithNavController(navController)
        binding.bottomNavigationView.isItemActiveIndicatorEnabled = false
    }

    private fun setupNavController(navController: NavController) {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.searchFragment, R.id.mediaLibraryFragment, R.id.settingsFragment -> {
                    showBottomNavigationView()
                }

                else -> {
                    hideBottomNavigationView()
                }
            }
        }
    }

    private fun showBottomNavigationView() {
        binding.dividerView.visibility = View.VISIBLE
        binding.bottomNavigationView.visibility = View.VISIBLE
    }

    private fun hideBottomNavigationView() {
        binding.dividerView.visibility = View.GONE
        binding.bottomNavigationView.visibility = View.GONE
    }
}