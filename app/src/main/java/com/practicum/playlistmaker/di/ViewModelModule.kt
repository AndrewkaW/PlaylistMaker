package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.ui.media.view_model.FavoritesViewModel
import com.practicum.playlistmaker.ui.media.view_model.PlaylistViewModel
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SettingsViewModel(get(), get())
    }

    viewModel {
        PlayerViewModel(get())
    }

    viewModel {
        SearchViewModel(get())
    }

    viewModel {
        FavoritesViewModel()
    }

    viewModel {
        PlaylistViewModel()
    }
}