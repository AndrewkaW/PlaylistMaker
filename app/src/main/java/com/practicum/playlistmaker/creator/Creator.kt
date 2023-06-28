package com.practicum.playlistmaker.creator

import android.content.Context
import android.media.MediaPlayer
import com.practicum.playlistmaker.Track
import com.practicum.playlistmaker.data.sharing.SharingStorage
import com.practicum.playlistmaker.data.sharing.impl.SharingRepositoryImpl
import com.practicum.playlistmaker.data.sharing.impl.SharingStorageImpl
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.domain.sharing.SharingInteractor
import com.practicum.playlistmaker.domain.sharing.SharingRepository
import com.practicum.playlistmaker.domain.sharing.impl.SharingInteractorImpl
import com.practicum.playlistmaker.presentation.player.PlayerPresenter

object Creator {

    //Player

    private fun providePlayerInteractor(
        track: Track,
        mediaPlayer: MediaPlayer,
    ): PlayerInteractor {
        return PlayerInteractorImpl(track, mediaPlayer)
    }

    fun providePlayerPresenter(track: Track, mediaPlayer: MediaPlayer): PlayerPresenter {
        return PlayerPresenter(providePlayerInteractor(track, mediaPlayer), track)
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
}