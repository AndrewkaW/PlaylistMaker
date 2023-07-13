package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.data.player.impl.PlayerRepositoryImpl
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    factory <SharingInteractor> {
        SharingInteractorImpl(get())
    }

    //Settings
    factory <SettingsInteractor> {
        SettingsInteractorImpl(get())
    }

    //Player
    factory<PlayerInteractor> {
        PlayerInteractorImpl(get())
    }
}