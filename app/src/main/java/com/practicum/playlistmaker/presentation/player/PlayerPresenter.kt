package com.practicum.playlistmaker.presentation.player


import android.os.Handler
import android.os.Looper
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Track
import com.practicum.playlistmaker.domain.Constans.Companion.DELAY_MILLIS
import com.practicum.playlistmaker.domain.Constans.Companion.REFRESH_PLAY_TIME_MILLIS
import com.practicum.playlistmaker.domain.Constans.Companion.START_PLAY_TIME_MILLIS
import com.practicum.playlistmaker.domain.Constans.Companion.STATE_DEFAULT
import com.practicum.playlistmaker.domain.Constans.Companion.STATE_PAUSED
import com.practicum.playlistmaker.domain.Constans.Companion.STATE_PLAYING
import com.practicum.playlistmaker.domain.Constans.Companion.STATE_PREPARED
import com.practicum.playlistmaker.domain.api.PlayerInteractor
import com.practicum.playlistmaker.utils.DateUtils.millisToStrFormat
import com.practicum.playlistmaker.utils.DateUtils.strDateFormat

class PlayerPresenter(private val player: PlayerInteractor, val track: Track) {
    private var view: PlayerView? = null
    private var mainThreadHandler = Handler(Looper.getMainLooper())

    fun attachView(view: PlayerView) {
        this.view = view
        view.setTrackName(track.trackName)
        view.setArtistName(track.artistName)
        view.setTrackTime(millisToStrFormat(track.trackTimeMillis))
        view.setArtwork(getArtworkUrl())
        view.setCollection(track.collectionName)
        view.setCollectionVisibility(isCollectionVisible())
        view.setReleaseDate(getReleaseDate())
        view.setPrimaryGenre(track.primaryGenreName)
        view.setCountry(track.country)
        view.setPlayButtonEnabled(false)
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
        updateTimeAndButton()
    }

    fun pausePlayer() {
        player.pausePlayer()
        view?.setPlayButtonImage(R.drawable.ic_play)
        mainThreadHandler.removeCallbacksAndMessages(null)
    }

    fun updateTimeAndButton() {

        mainThreadHandler.postDelayed(
            object : Runnable {
                override fun run() {
                    conditionPlayButton()
                    // Обновляем время
                    val currentTime = player.getCurrentTime()
                    if (currentTime < REFRESH_PLAY_TIME_MILLIS) {
                        view?.setPlayTimeText(millisToStrFormat(currentTime))
                    } else {
                        view?.setPlayTimeText(millisToStrFormat(START_PLAY_TIME_MILLIS))
                        view?.setPlayButtonImage(R.drawable.ic_play)
                    }
                    // И снова планируем то же действие через 0.5 сек
                    mainThreadHandler.postDelayed(
                        this,
                        DELAY_MILLIS,
                    )
                }
            },
            DELAY_MILLIS
        )

    }

    private fun isCollectionVisible(): Boolean = track.collectionName.isNotEmpty()

    private fun getArtworkUrl(): String = track.artworkUrl100.replaceAfterLast('/', "512x512bb.jpg")

    private fun getReleaseDate(): String = strDateFormat(track.releaseDate)

    private fun conditionPlayButton() {
        view?.setPlayButtonEnabled(
            player.getPlayerState() != STATE_DEFAULT
        )

    }

}