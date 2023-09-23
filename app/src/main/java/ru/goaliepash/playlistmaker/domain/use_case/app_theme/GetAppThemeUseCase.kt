package ru.goaliepash.playlistmaker.domain.use_case.app_theme

import ru.goaliepash.playlistmaker.domain.api.AppThemeRepository

class GetAppThemeUseCase(private val appThemeRepository: AppThemeRepository) {

    operator fun invoke(): Boolean {
        return appThemeRepository.isDarkThemeEnabled()
    }
}