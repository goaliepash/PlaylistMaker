package ru.goaliepash.playlistmaker.ui.listener

import ru.goaliepash.domain.model.Playlist

fun interface OnPlaylistClickListener {
    fun onPlaylistClick(playlist: Playlist)
}