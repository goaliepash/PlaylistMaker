package ru.goaliepash.data.shared_preferences.app_theme

import android.content.SharedPreferences

class AppThemeClientImpl(private val sharedPreferences: SharedPreferences) : AppThemeClient {

    override fun get(): Boolean {
        return sharedPreferences.getBoolean(DARK_THEME_KEY, false)
    }

    override fun set(darkThemeEnabled: Boolean) {
        sharedPreferences.edit().putBoolean(DARK_THEME_KEY, darkThemeEnabled).apply()
    }

    companion object {
        private const val DARK_THEME_KEY = "DARK_THEME_KEY"
    }
}