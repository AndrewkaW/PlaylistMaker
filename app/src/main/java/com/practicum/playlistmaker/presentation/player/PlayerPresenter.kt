package com.practicum.playlistmaker.presentation.player


import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.Constans.Companion.DEFAULT_PLAY_TIME
import com.practicum.playlistmaker.domain.Constans.Companion.DELAY
import com.practicum.playlistmaker.domain.Constans.Companion.REFRESH_PLAY_TIME
import com.practicum.playlistmaker.domain.Constans.Companion.STATE_PAUSED
import com.practicum.playlistmaker.domain.Constans.Companion.STATE_PLAYING
import com.practicum.playlistmaker.domain.Constans.Companion.STATE_PREPARED
import com.practicum.playlistmaker.domain.api.PlayerUseCase
import com.practicum.playlistmaker.utils.DateUtils.millisToStrFormat

class PlayerPresenter(private val player: PlayerUseCase) {
    private var view: PlayerView? = null
    private var mainThreadHandler = Handler(Looper.getMainLooper())

    fun attachView(view: PlayerView) {
        this.view = view
        view.setTrackName(player.getTrackName())
        view.setArtistName(player.getArtistName())
        view.setTrackTime(millisToStrFormat(player.getTrackTime()))
        view.setArtwork(player.getArtworkUrl())
        view.setCollection(player.getCollectionName())
        view.setCollectionVisibility(player.isCollectionVisible())
        view.setReleaseDate(player.getReleaseDate())
        view.setPrimaryGenre(player.getPrimaryGenre())
        view.setCountry(player.getCountry())
        view.setPlayButtonEnabled(false)
        view.setPlayTimeText(DEFAULT_PLAY_TIME)
    }

    fun detachView() {
        this.view = null
        mainThreadHandler.removeCallbacksAndMessages(null)
        player.releasePlayer()
    }

    fun playbackControl() {
        when (player.getPlayerState()) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED, STATE_PAUSED -> {
                startPlayer()
            }
        }
    }

    private fun startPlayer() {
        player.startPlayer()
        view?.setPlayButtonImage(R.drawable.ic_pause)
    }

    fun pausePlayer() {
        player.pausePlayer()
        view?.setPlayButtonImage(R.drawable.ic_play)
        mainThreadHandler.removeCallbacksAndMessages(null)
    }

    fun updateCurrentTime() {


        mainThreadHandler.postDelayed(
            object : Runnable {
                override fun run() {
                    // Обновляем время
                    val currentTime = player.getCurrentTime()
                    view?.setPlayTimeText(if (currentTime < REFRESH_PLAY_TIME) {
                        millisToStrFormat(currentTime)
                    } else {
                        millisToStrFormat(0)
                    })
                    // И снова планируем то же действие через 0.5 сек
                    mainThreadHandler.postDelayed(
                        this,
                        DELAY,
                    )
                }
            },
            DELAY
        )

    }
}