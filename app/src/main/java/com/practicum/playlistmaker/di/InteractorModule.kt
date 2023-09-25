package com.practicum.playlistmaker.di

import com.practicum.playlistmaker.domain.favorites.FavoritesInteractor
import com.practicum.playlistmaker.domain.favorites.impl.FavoritesInteractorImpl
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.search.SearchInteractor
import com.practicum.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import org.koin.dsl.module

val interactorModule = module {
    //Sharing
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

    //Search
    factory <SearchInteractor> {
        SearchInteractorImpl(get())
    }

    //Favorites
    factory <FavoritesInteractor> {
        FavoritesInteractorImpl(get())
    }
}