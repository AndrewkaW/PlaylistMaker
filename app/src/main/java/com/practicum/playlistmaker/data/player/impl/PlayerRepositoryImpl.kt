package com.practicum.playlistmaker.data.player.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.Constants
import com.practicum.playlistmaker.domain.player.PlayerRepository

class PlayerRepositoryImpl(private val mediaPlayer: MediaPlayer) :
    PlayerRepository {

    private var playerState = Constants.STATE_DEFAULT

    init {
        mediaPlayer.setOnPreparedListener {
            playerState = Constants.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = Constants.STATE_PREPARED
        }

    }

    override fun prepareTrack(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = Constants.STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = Constants.STATE_PAUSED
    }

    override fun releasePlayer() {
        playerState = Constants.STATE_DEFAULT
        mediaPlayer.release()
    }

    override fun getPlayerState(): Int {
        return playerState
    }

    override fun getCurrentTime(): Int {
        return mediaPlayer.currentPosition
    }
}