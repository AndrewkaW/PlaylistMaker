package com.practicum.playlistmaker.creator

import android.app.Application
import android.content.Context
import android.media.MediaPlayer
import com.practicum.playlistmaker.Track
import com.practicum.playlistmaker.data.settings.ThemeStorage
import com.practicum.playlistmaker.data.settings.impl.SettingsRepositoryImpl
import com.practicum.playlistmaker.data.settings.impl.ThemeStorageImpl
import com.practicum.playlistmaker.data.sharing.SharingStorage
import com.practicum.playlistmaker.data.sharing.impl.SharingRepositoryImpl
import com.practicum.playlistmaker.data.sharing.impl.SharingStorageImpl
import com.practicum.playlistmaker.domain.Constants.Companion.APP_SETTINGS
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.settings.SettingsInteractor
import com.practicum.playlistmaker.domain.settings.SettingsRepository
import com.practicum.playlistmaker.domain.settings.impl.SettingsInteractorImpl
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.SharingRepository
import com.practicum.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import com.practicum.playlistmaker.ui.player.view_model.PlayerViewModel

object Creator {

    //Player

    fun providePlayerInteractor(
        track: Track,
        mediaPlayer: MediaPlayer,
    ): PlayerInteractor {
        return PlayerInteractorImpl(track, mediaPlayer)
    }

//    fun providePlayerViewModel(track: Track, mediaPlayer: MediaPlayer): PlayerViewModel {
//        return PlayerViewModel(providePlayerInteractor(track, mediaPlayer), track)
//    }

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
}