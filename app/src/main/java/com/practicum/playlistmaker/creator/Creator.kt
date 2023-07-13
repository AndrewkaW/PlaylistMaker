package com.practicum.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.content.Context.MODE_PRIVATE
import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.data.player.impl.PlayerRepositoryImpl
import com.practicum.playlistmaker.data.search.HistoryStorage
import com.practicum.playlistmaker.data.search.NetworkClient
import com.practicum.playlistmaker.data.search.impl.HistoryStorageImpl
import com.practicum.playlistmaker.data.search.impl.SearchRepositoryImpl
import com.practicum.playlistmaker.data.search.network.RetrofitNetworkClient
import com.practicum.playlistmaker.data.settings.ThemeStorage
import com.practicum.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.data.settings.impl.ThemeStorageImpl
import com.practicum.playlistmaker.data.sharing.SharingStorage
import com.practicum.playlistmaker.data.sharing.impl.SharingRepositoryImpl
import com.practicum.playlistmaker.data.sharing.impl.SharingStorageImpl
import com.practicum.playlistmaker.domain.Constants.Companion.APP_SETTINGS
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.PlayerRepository
import com.practicum.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.search.SearchInteractor
import com.practicum.playlistmaker.domain.search.SearchRepository
import com.practicum.playlistmaker.domain.search.impl.SearchInteractorImpl
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.SettingsRepository
import com.practicum.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.SharingRepository
import com.practicum.playlistmaker.domain.sharing.impl.SharingInteractorImpl

object Creator {

    //Player

    fun providePlayerInteractor(track: Track): PlayerInteractor {
        return PlayerInteractorImpl(providePlayerRepository(track))
    }

    private fun providePlayerRepository(track: Track): PlayerRepository {
        return PlayerRepositoryImpl(
            track,
            MediaPlayer()
        )
    }

    //Sharing

    fun provideSharingInteractor(context: Context): SharingInteractor {
        return SharingInteractorImpl(provideSharingRepository(context))
    }

    private fun provideSharingStorage(context: Context): SharingStorage {
        return SharingStorageImpl(context)
    }

    private fun provideSharingRepository(context: Context): SharingRepository {
        return SharingRepositoryImpl(context, provideSharingStorage(context))
    }

    //Settings

    fun provideSettingsInteractor(context: Context): SettingsInteractor {
        return SettingsInteractorImpl(provideSettingsRepository(context))
    }

    private fun provideSettingsRepository(context: Context): SettingsRepository {
        return SettingsRepositoryImpl(provideThemeStorage(context))
    }

    private fun provideThemeStorage(context: Context): ThemeStorage {
        return ThemeStorageImpl(
            context.getSharedPreferences(
                APP_SETTINGS,
                Application.MODE_PRIVATE
            ),
            context
        )
    }

    //Search

    private fun provideHistoryStorage(context: Context): HistoryStorage {
        return HistoryStorageImpl(
            context.getSharedPreferences(
                APP_SETTINGS, MODE_PRIVATE
            )
        )
    }

    private fun provideNetworkClient(context: Context): NetworkClient {
        return RetrofitNetworkClient(context)
    }

    private fun provideSearchRepository(context: Context): SearchRepository {
        return SearchRepositoryImpl(
            provideNetworkClient(context),
            provideHistoryStorage(context)
        )
    }

    fun provideSearchInteractor(context: Context): SearchInteractor {
        return SearchInteractorImpl(provideSearchRepository(context))
    }
}