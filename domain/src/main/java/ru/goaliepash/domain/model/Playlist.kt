package ru.goaliepash.domain.model

data class Playlist(
    val id: Int = 0,
    val name: String,
    val description: String,
    val trackIds: List<String>,
    val tracksCount: Int,
    val coverUri: String,
    val dateAdded: Long
)