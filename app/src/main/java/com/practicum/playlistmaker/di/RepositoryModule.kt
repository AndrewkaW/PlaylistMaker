package com.practicum.playlistmaker.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.data.convertors.PlaylistDbConvertor
import com.practicum.playlistmaker.data.convertors.TrackDbConvertor
import com.practicum.playlistmaker.data.favorites.impl.FavoritesRepositoryImpl
import com.practicum.playlistmaker.data.player.impl.PlayerRepositoryImpl
import com.practicum.playlistmaker.data.playlists.PlaylistsRepositoryImpl
import com.practicum.playlistmaker.data.search.impl.SearchRepositoryImpl
import com.practicum.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.data.sharing.impl.SharingRepositoryImpl
import com.practicum.playlistmaker.domain.favorites.FavoritesRepository
import com.practicum.playlistmaker.domain.player.PlayerRepository
import com.practicum.playlistmaker.domain.playlists.PlaylistsRepository
import com.practicum.playlistmaker.domain.search.SearchRepository
import com.practicum.playlistmaker.domain.settings.SettingsRepository
import com.practicum.playlistmaker.domain.sharing.SharingRepository
import org.koin.dsl.module

val repositoryModule = module {

    single<SharingRepository> {
        SharingRepositoryImpl(get(), get())
    }

    single<SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    factory<PlayerRepository> {
        PlayerRepositoryImpl(get(), get(), get())
    }

    factory {
        MediaPlayer()
    }

    single<SearchRepository> {
        SearchRepositoryImpl(get(), get(), get())
    }

    factory { TrackDbConvertor() }

    single<FavoritesRepository> {
        FavoritesRepositoryImpl(get(), get())
    }

    factory {
        PlaylistDbConvertor(get())
    }

    single<PlaylistsRepository> {
        PlaylistsRepositoryImpl(get(), get(), get())
    }
}