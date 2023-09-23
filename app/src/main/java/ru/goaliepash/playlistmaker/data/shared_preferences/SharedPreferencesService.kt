package ru.goaliepash.playlistmaker.data.shared_preferences

import android.content.Context
import android.content.SharedPreferences

object SharedPreferencesService {

    private const val PLAYLIST_MAKER_PREFERENCES = "PLAYLIST_MAKER_PREFERENCES"

    fun get(context: Context): SharedPreferences {
        return context.getSharedPreferences(PLAYLIST_MAKER_PREFERENCES, Context.MODE_PRIVATE)
    }
}