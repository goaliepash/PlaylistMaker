package ru.goaliepash.playlistmaker.di

import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module
import ru.goaliepash.playlistmaker.presentation.view_model.AudioPlayerViewModel
import ru.goaliepash.playlistmaker.presentation.view_model.FavoriteTracksViewModel
import ru.goaliepash.playlistmaker.presentation.view_model.NewPlaylistViewModel
import ru.goaliepash.playlistmaker.presentation.view_model.PlaylistsViewModel
import ru.goaliepash.playlistmaker.presentation.view_model.SearchViewModel
import ru.goaliepash.playlistmaker.presentation.view_model.SettingsViewModel

val appModule = module {

    viewModel {
        SearchViewModel(itunesInteractor = get(), searchHistoryInteractor = get())
    }

    viewModel {
        SettingsViewModel(appThemeInteractor = get())
    }

    viewModel {
        FavoriteTracksViewModel(favoriteTracksInteractor = get())
    }

    viewModel {
        PlaylistsViewModel(playlistsInteractor = get())
    }

    viewModel {
        AudioPlayerViewModel(favoriteTracksInteractor = get(), playlistsInteractor = get())
    }

    viewModel {
        NewPlaylistViewModel(playlistInteractor = get())
    }
}