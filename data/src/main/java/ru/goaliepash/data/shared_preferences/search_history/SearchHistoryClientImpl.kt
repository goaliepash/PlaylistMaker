package ru.goaliepash.data.shared_preferences.search_history

import android.content.Context
import com.google.gson.Gson
import ru.goaliepash.data.shared_preferences.SharedPreferencesService
import ru.goaliepash.domain.model.Track

class SearchHistoryClientImpl(context: Context) : SearchHistoryClient {

    private val sharedPreferences = SharedPreferencesService.get(context)

    override fun add(searchHistoryTracks: List<Track>) {
        sharedPreferences.edit().putString(SEARCH_HISTORY_KEY, createJsonFromTrackList(searchHistoryTracks)).apply()
    }

    override fun get(): Array<Track> {
        return createTracksFromJson(sharedPreferences.getString(SEARCH_HISTORY_KEY, JSON_EMPTY_LIST) ?: JSON_EMPTY_LIST)
    }

    override fun clear() {
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