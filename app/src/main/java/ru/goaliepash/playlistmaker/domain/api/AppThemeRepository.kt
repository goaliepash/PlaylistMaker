package ru.goaliepash.playlistmaker.domain.api

interface AppThemeRepository {

    fun isDarkThemeEnabled(): Boolean

    fun setDarkThemeEnabled(darkThemeEnabled: Boolean)
}