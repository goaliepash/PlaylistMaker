package ru.goaliepash.playlistmaker.ui.listener

import ru.goaliepash.domain.model.Track

fun interface OnTrackLongClickListener {
    fun onTrackLongClick(track: Track): Boolean
}