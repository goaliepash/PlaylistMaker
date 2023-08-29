package ru.goaliepash.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson

class SearchHistory(private val sharedPreferences: SharedPreferences) {

    fun add(searchHistoryTracks: List<Track>) {
        sharedPreferences.edit().putString(SEARCH_HISTORY_KEY, createJsonFromTrackList(searchHistoryTracks)).apply()
    }

    fun get(): Array<Track> {
        return createTracksFromJson(sharedPreferences.getString(SEARCH_HISTORY_KEY, JSON_EMPTY_LIST) ?: JSON_EMPTY_LIST)
    }

    fun clear() {
        sharedPreferences.edit().clear().apply()
    }

    private fun createJsonFromTrackList(tracks: List<Track>): String {
        return Gson().toJson(tracks)
    }

    private fun createTracksFromJson(json: String): Array<Track> {
        return Gson().fromJson(json, Array<Track>::class.java)
    }

    companion object {
        private const val SEARCH_HISTORY_KEY = "SEARCH_HISTORY_KEY"
        private const val JSON_EMPTY_LIST = "[]"
    }
}