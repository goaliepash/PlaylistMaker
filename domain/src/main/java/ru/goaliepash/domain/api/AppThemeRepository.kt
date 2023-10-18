package ru.goaliepash.domain.api

interface AppThemeRepository {

    fun isDarkThemeEnabled(): Boolean

    fun setDarkThemeEnabled(darkThemeEnabled: Boolean)
}