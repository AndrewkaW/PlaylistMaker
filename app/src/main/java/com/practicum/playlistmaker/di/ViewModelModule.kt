package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.settings.SettingsRepository
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel
import com.practicum.playlistmaker.ui.settings.view_model.SettingsViewModel
import org.koin.androidx.viewmodel.dsl.viewModel
import org.koin.dsl.module

val viewModelModule = module {

    viewModel {
        SettingsViewModel(get(),get())
    }

    viewModel { (track: Track) ->
        PlayerViewModel(get(),track)
    }

}