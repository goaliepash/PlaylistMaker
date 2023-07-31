package ru.goaliepash.playlistmaker

import ru.goaliepash.playlistmaker.model.Track

fun interface OnTrackClickListener {
    fun onTrackClick(track: Track)
}