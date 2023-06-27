package com.practicum.playlistmaker.utils

import android.media.MediaPlayer
import com.practicum.playlistmaker.Track
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.impl.PlayerInteractorImpl
import com.practicum.playlistmaker.presentation.player.PlayerPresenter

object Creator {

    private fun providePlayerInteractor(
        track: Track,
        mediaPlayer: MediaPlayer,
    ): PlayerInteractor {
        return PlayerInteractorImpl(track, mediaPlayer)
    }

    fun providePlayerPresenter(track: Track, mediaPlayer: MediaPlayer): PlayerPresenter {
        return PlayerPresenter(providePlayerInteractor(track, mediaPlayer), track)
    }
}