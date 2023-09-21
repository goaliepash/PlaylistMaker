package ru.goaliepash.playlistmaker.domain.api

import ru.goaliepash.playlistmaker.domain.model.Track

interface SearchHistoryRepository {

    fun add(searchHistoryTracks: List<Track>)

    fun get(): Array<Track>

    fun clear()
}