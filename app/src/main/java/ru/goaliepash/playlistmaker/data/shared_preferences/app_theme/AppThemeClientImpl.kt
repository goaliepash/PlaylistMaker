package ru.goaliepash.playlistmaker.data.shared_preferences.app_theme

import android.content.Context
import ru.goaliepash.playlistmaker.data.shared_preferences.SharedPreferencesService

class AppThemeClientImpl(context: Context) : AppThemeClient {

    private val sharedPreferences = SharedPreferencesService.get(context)

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