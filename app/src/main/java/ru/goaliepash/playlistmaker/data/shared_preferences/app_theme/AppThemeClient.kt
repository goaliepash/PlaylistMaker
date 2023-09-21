package ru.goaliepash.playlistmaker.data.shared_preferences.app_theme

interface AppThemeClient {

    fun get(): Boolean

    fun set(darkThemeEnabled: Boolean)
}