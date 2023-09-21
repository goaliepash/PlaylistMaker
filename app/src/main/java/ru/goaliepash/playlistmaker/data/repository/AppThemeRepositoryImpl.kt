package ru.goaliepash.playlistmaker.data.repository

import ru.goaliepash.playlistmaker.data.shared_preferences.app_theme.AppThemeClient
import ru.goaliepash.playlistmaker.domain.api.AppThemeRepository

class AppThemeRepositoryImpl(private val appThemeClient: AppThemeClient) : AppThemeRepository {

    override fun get(): Boolean {
        return appThemeClient.get()
    }

    override fun set(darkThemeEnabled: Boolean) {
        appThemeClient.set(darkThemeEnabled)
    }
}