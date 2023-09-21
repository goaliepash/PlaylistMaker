package ru.goaliepash.playlistmaker.domain.api

interface AppThemeRepository {

    fun get(): Boolean

    fun set(darkThemeEnabled: Boolean)
}