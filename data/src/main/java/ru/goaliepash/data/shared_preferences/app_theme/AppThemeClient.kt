package ru.goaliepash.data.shared_preferences.app_theme

interface AppThemeClient {

    fun get(): Boolean

    fun set(darkThemeEnabled: Boolean)
}