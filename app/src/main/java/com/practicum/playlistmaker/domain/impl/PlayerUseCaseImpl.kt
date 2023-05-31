package com.practicum.playlistmaker.domain.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Track
import com.practicum.playlistmaker.domain.Constans.Companion.DEFAULT_PLAY_TIME
import com.practicum.playlistmaker.domain.Constans.Companion.STATE_DEFAULT
import com.practicum.playlistmaker.domain.Constans.Companion.STATE_PAUSED
import com.practicum.playlistmaker.domain.Constans.Companion.STATE_PLAYING
import com.practicum.playlistmaker.domain.Constans.Companion.STATE_PREPARED
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.domain.api.PlayerUseCase
import java.text.SimpleDateFormat
import java.util.*

class PlayerUseCaseImpl(private val track: Track, private val mediaPlayer: MediaPlayer, private val playerInteractor: PlayerInteractor) : PlayerUseCase {

    private var playerState = STATE_DEFAULT

    init {
        mediaPlayer.setDataSource(track.previewUrl)
        mediaPlayer.prepareAsync()
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
            playerInteractor.setPlayButtonEnabled(true)
        }
        mediaPlayer.setOnCompletionListener {
            playerInteractor.setPlayButtonImage(R.drawable.ic_play)
            playerInteractor.setPlayTimeText(DEFAULT_PLAY_TIME)
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

    override fun getTrackName(): String {
        return track.trackName
    }

    override fun getArtistName(): String {
        return track.artistName
    }

    override fun getTrackTime(): Int {
        return track.trackTimeMillis
    }

    override fun getArtworkUrl(): String {
        return track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")
    }

    override fun getCollectionName(): String {
        return track.collectionName
    }

    override fun isCollectionVisible(): Boolean {
        return track.collectionName.isNotEmpty()
    }

    override fun getReleaseDate(): String {
        val formatDate = SimpleDateFormat("yyyy", Locale.getDefault()).parse(track.releaseDate)
        return SimpleDateFormat("yyyy", Locale.getDefault()).format(formatDate!!)
    }

    override fun getPrimaryGenre(): String {
        return track.primaryGenreName
    }

    override fun getCountry(): String {
        return track.country
    }

}