package ru.goaliepash.playlistmaker

data class SearchResponse(
    val resultCount: Int,
    val results: List<Track>
)