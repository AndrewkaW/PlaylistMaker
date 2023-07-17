package com.practicum.playlistmaker.di

import android.media.MediaPlayer
import com.practicum.playlistmaker.data.player.impl.PlayerRepositoryImpl
import com.practicum.playlistmaker.data.search.impl.SearchRepositoryImpl
import com.practicum.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.data.sharing.impl.SharingRepositoryImpl
import com.practicum.playlistmaker.domain.player.PlayerRepository
import com.practicum.playlistmaker.domain.search.SearchRepository
import com.practicum.playlistmaker.domain.settings.SettingsRepository
import com.practicum.playlistmaker.domain.sharing.SharingRepository
import org.koin.dsl.module

val repositoryModule  = module {

    single<SharingRepository> {
        SharingRepositoryImpl(get(), get())
    }

    //Settings
    single <SettingsRepository> {
        SettingsRepositoryImpl(get())
    }

    //Player
    factory <PlayerRepository> {
        PlayerRepositoryImpl(get())
    }

    factory <MediaPlayer> {
        MediaPlayer()
    }

    //Search
    single <SearchRepository> {
        SearchRepositoryImpl(get(),get())
    }


}