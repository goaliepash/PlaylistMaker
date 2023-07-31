package ru.goaliepash.playlistmaker

import android.app.Application
import android.content.SharedPreferences
import androidx.appcompat.app.AppCompatDelegate

class App : Application() {

    var darkTheme = false

    private lateinit var sharedPreferences: SharedPreferences

    override fun onCreate() {
        super.onCreate()
        sharedPreferences = getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, MODE_PRIVATE)
        setTheme(sharedPreferences.getBoolean(DARK_THEME_KEY, false))
    }

    fun switchTheme(darkThemeEnabled: Boolean) {
        setTheme(darkThemeEnabled)
        saveDarkThemeStatus(darkThemeEnabled)
    }

    private fun setTheme(darkThemeEnabled: Boolean) {
        this.darkTheme = darkThemeEnabled
        AppCompatDelegate.setDefaultNightMode(
            if (darkThemeEnabled) {
                AppCompatDelegate.MODE_NIGHT_YES
            } else {
                AppCompatDelegate.MODE_NIGHT_NO
            }
        )
    }

    private fun saveDarkThemeStatus(darkThemeEnabled: Boolean) = sharedPreferences.edit().putBoolean(DARK_THEME_KEY, darkThemeEnabled).apply()

    companion object {
        private const val PLAYLIST_MAKER_PREFERENCES = "PLAYLIST_MAKER_PREFERENCES"
        private const val DARK_THEME_KEY = "DARK_THEME_KEY"
    }
}