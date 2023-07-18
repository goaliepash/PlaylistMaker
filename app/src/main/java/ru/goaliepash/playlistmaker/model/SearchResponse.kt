package ru.goaliepash.playlistmaker.model

data class SearchResponse(
    val resultCount: Int,
    val results: List<Track>
)