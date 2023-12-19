package ru.goaliepash.playlistmaker.presentation.state

import androidx.annotation.DrawableRes
import ru.goaliepash.playlistmaker.R

sealed class AudioPlayerState(val isPlayButtonEnabled: Boolean, @DrawableRes val imageResource: Int, val progress: String) {

    class Default : AudioPlayerState(false, R.drawable.ic_play, "00:00")

    class Prepared : AudioPlayerState(true, R.drawable.ic_play, "00:00")

    class Playing(progress: String) : AudioPlayerState(true, R.drawable.ic_pause, progress)

    class Paused(progress: String) : AudioPlayerState(true, R.drawable.ic_play, progress)
}