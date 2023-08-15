package com.practicum.playlistmaker.data.player.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.STATE_DEFAULT
import com.practicum.playlistmaker.domain.STATE_PAUSED
import com.practicum.playlistmaker.domain.STATE_PLAYING
import com.practicum.playlistmaker.domain.STATE_PREPARED
import com.practicum.playlistmaker.domain.player.PlayerRepository

class PlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) :
    PlayerRepository {

    private var playerState = STATE_DEFAULT


    init {
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
        }

    }

    override fun prepareTrack(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
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
        playerState = STATE_DEFAULT
        mediaPlayer.release()
    }

    override fun getPlayerState(): Int {
        return playerState
    }

    override fun getCurrentTime(): Int {
        return mediaPlayer.currentPosition
    }
}