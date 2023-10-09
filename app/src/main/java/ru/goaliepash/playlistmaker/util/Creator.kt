package ru.goaliepash.playlistmaker.util

import android.content.Context
import ru.goaliepash.playlistmaker.data.network.itunes.ItunesClient
import ru.goaliepash.playlistmaker.data.network.itunes.ItunesClientImpl
import ru.goaliepash.playlistmaker.data.repository.AppThemeRepositoryImpl
import ru.goaliepash.playlistmaker.data.repository.ItunesRepositoryImpl
import ru.goaliepash.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import ru.goaliepash.playlistmaker.data.shared_preferences.app_theme.AppThemeClient
import ru.goaliepash.playlistmaker.data.shared_preferences.app_theme.AppThemeClientImpl
import ru.goaliepash.playlistmaker.data.shared_preferences.search_history.SearchHistoryClient
import ru.goaliepash.playlistmaker.data.shared_preferences.search_history.SearchHistoryClientImpl
import ru.goaliepash.playlistmaker.domain.api.AppThemeRepository
import ru.goaliepash.playlistmaker.domain.api.ItunesRepository
import ru.goaliepash.playlistmaker.domain.use_case.app_theme.GetAppThemeUseCase
import ru.goaliepash.playlistmaker.domain.use_case.app_theme.SetAppThemeUseCase
import ru.goaliepash.playlistmaker.domain.use_case.itunes.GetSearchUseCase
import ru.goaliepash.playlistmaker.domain.use_case.search_history.AddSearchHistoryUseCase
import ru.goaliepash.playlistmaker.domain.use_case.search_history.ClearSearchHistoryUseCase
import ru.goaliepash.playlistmaker.domain.use_case.search_history.GetSearchHistoryUseCase

object Creator {

    fun provideGetAppThemeUseCase(context: Context): GetAppThemeUseCase {
        return GetAppThemeUseCase(provideAppThemeRepository(context))
    }

    fun provideSetAppThemeUseCase(context: Context): SetAppThemeUseCase {
        return SetAppThemeUseCase(provideAppThemeRepository(context))
    }

    fun provideGetSearchUseCase(): GetSearchUseCase {
        return GetSearchUseCase(provideItunesRepository())
    }

    fun provideAddSearchHistoryUseCase(context: Context): AddSearchHistoryUseCase {
        return AddSearchHistoryUseCase(provideSearchHistoryRepository(context))
    }

    fun provideGetSearchHistoryUseCase(context: Context): GetSearchHistoryUseCase {
        return GetSearchHistoryUseCase(provideSearchHistoryRepository(context))
    }

    fun provideClearSearchHistoryUseCase(context: Context): ClearSearchHistoryUseCase {
        return ClearSearchHistoryUseCase(provideSearchHistoryRepository(context))
    }

    private fun provideAppThemeRepository(context: Context): AppThemeRepository {
        return AppThemeRepositoryImpl(provideAppThemeClient(context))
    }

    private fun provideItunesRepository(): ItunesRepository {
        return ItunesRepositoryImpl(provideItunesClient())
    }

    private fun provideSearchHistoryRepository(context: Context): SearchHistoryRepositoryImpl {
        return SearchHistoryRepositoryImpl(provideSearchHistoryClient(context))
    }

    private fun provideAppThemeClient(context: Context): AppThemeClient {
        return AppThemeClientImpl(context)
    }

    private fun provideItunesClient(): ItunesClient {
        return ItunesClientImpl()
    }

    private fun provideSearchHistoryClient(context: Context): SearchHistoryClient {
        return SearchHistoryClientImpl(context)
    }
}