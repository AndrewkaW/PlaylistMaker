package com.practicum.playlistmaker.data.player.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.Constants
import com.practicum.playlistmaker.domain.player.PlayerRepository

class PlayerRepositoryImpl(track: Track, private val mediaPlayer: MediaPlayer) :
    PlayerRepository {

    private var playerState = Constants.STATE_DEFAULT

    init {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = Constants.STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = Constants.STATE_PREPARED
        }
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
        mediaPlayer.release()
    }

    override fun getPlayerState(): Int {
        return playerState
    }

    override fun getCurrentTime(): Int {
        return mediaPlayer.currentPosition
    }
}