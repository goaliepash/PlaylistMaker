package ru.goaliepash.playlistmaker.ui.listener

import ru.goaliepash.domain.model.Track

fun interface OnTrackClickListener {
    fun onTrackClick(track: Track)
}