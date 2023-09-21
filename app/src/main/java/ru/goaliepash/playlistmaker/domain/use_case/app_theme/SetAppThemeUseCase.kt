package ru.goaliepash.playlistmaker.domain.use_case.app_theme

import ru.goaliepash.playlistmaker.domain.api.AppThemeRepository

class SetAppThemeUseCase(private val appThemeRepository: AppThemeRepository) {

    fun execute(darkThemeEnabled: Boolean) {
        appThemeRepository.set(darkThemeEnabled)
    }
}