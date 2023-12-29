package ru.goaliepash.playlistmaker.di

import android.content.Context
import android.content.SharedPreferences
import androidx.room.Room
import org.koin.android.ext.koin.androidContext
import org.koin.dsl.module
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory
import ru.goaliepash.data.converter.FavoriteTracksDbConverter
import ru.goaliepash.data.db.AppDatabase
import ru.goaliepash.data.itunes.ItunesClient
import ru.goaliepash.data.itunes.ItunesClientImpl
import ru.goaliepash.data.itunes.ItunesService
import ru.goaliepash.data.repository.AppThemeRepositoryImpl
import ru.goaliepash.data.repository.FavoriteTracksRepositoryImpl
import ru.goaliepash.data.repository.ItunesRepositoryImpl
import ru.goaliepash.data.repository.SearchHistoryRepositoryImpl
import ru.goaliepash.data.shared_preferences.app_theme.AppThemeClient
import ru.goaliepash.data.shared_preferences.app_theme.AppThemeClientImpl
import ru.goaliepash.data.shared_preferences.search_history.SearchHistoryClient
import ru.goaliepash.data.shared_preferences.search_history.SearchHistoryClientImpl
import ru.goaliepash.domain.api.AppThemeRepository
import ru.goaliepash.domain.api.FavoriteTracksRepository
import ru.goaliepash.domain.api.ItunesRepository
import ru.goaliepash.domain.api.SearchHistoryRepository

val dataModule = module {

    factory {
        FavoriteTracksDbConverter()
    }

    single<SharedPreferences> {
        androidContext().getSharedPreferences("PLAYLIST_MAKER_PREFERENCES", Context.MODE_PRIVATE)
    }

    single<AppThemeClient> {
        AppThemeClientImpl(sharedPreferences = get())
    }

    single<AppThemeRepository> {
        AppThemeRepositoryImpl(appThemeClient = get())
    }

    single {
        Room
            .databaseBuilder(androidContext(), AppDatabase::class.java, "database.db")
            .build()
    }

    single<ItunesService> {
        Retrofit
            .Builder()
            .baseUrl("https://itunes.apple.com")
            .addConverterFactory(GsonConverterFactory.create())
            .build()
            .create(ItunesService::class.java)
    }

    single<ItunesClient> {
        ItunesClientImpl(itunesService = get())
    }

    single<ItunesRepository> {
        ItunesRepositoryImpl(itunesClient = get())
    }

    single<SearchHistoryClient> {
        SearchHistoryClientImpl(sharedPreferences = get())
    }

    single<SearchHistoryRepository> {
        SearchHistoryRepositoryImpl(searchHistoryClient = get())
    }

    single<FavoriteTracksRepository> {
        FavoriteTracksRepositoryImpl(appDatabase = get(), favoriteTracksDbConverter = get())
    }
}