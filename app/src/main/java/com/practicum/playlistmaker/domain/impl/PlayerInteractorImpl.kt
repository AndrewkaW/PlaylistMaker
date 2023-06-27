package com.practicum.playlistmaker.domain.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.Track
import com.practicum.playlistmaker.domain.Constans.Companion.STATE_DEFAULT
import com.practicum.playlistmaker.domain.Constans.Companion.STATE_PAUSED
import com.practicum.playlistmaker.domain.Constans.Companion.STATE_PLAYING
import com.practicum.playlistmaker.domain.Constans.Companion.STATE_PREPARED
import com.practicum.playlistmaker.domain.api.PlayerInteractor

class PlayerInteractorImpl(track: Track, private val mediaPlayer: MediaPlayer) :
    PlayerInteractor {

    private var playerState = STATE_DEFAULT

    init {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
        }
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    override fun releasePlayer() {
        mediaPlayer.release()
    }

    override fun getPlayerState(): Int {
        return playerState
    }

    override fun getCurrentTime(): Int {
        return mediaPlayer.currentPosition
    }

}