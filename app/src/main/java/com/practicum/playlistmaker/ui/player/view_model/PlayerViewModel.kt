package com.practicum.playlistmaker.ui.player.view_model

import android.os.Handler
import android.os.Looper
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.Constants.Companion.DELAY_MILLIS
import com.practicum.playlistmaker.domain.Constants.Companion.REFRESH_PLAY_TIME_MILLIS
import com.practicum.playlistmaker.domain.Constants.Companion.START_PLAY_TIME_MILLIS
import com.practicum.playlistmaker.domain.Constants.Companion.STATE_DEFAULT
import com.practicum.playlistmaker.domain.Constants.Companion.STATE_PAUSED
import com.practicum.playlistmaker.domain.Constants.Companion.STATE_PLAYING
import com.practicum.playlistmaker.domain.Constants.Companion.STATE_PREPARED
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.utils.DateUtils.millisToStrFormat

class PlayerViewModel(private val player: PlayerInteractor) : ViewModel() {

    private val _playButtonEnabled = MutableLiveData<Boolean>()
    val playButtonEnabled: LiveData<Boolean> get() = _playButtonEnabled

    private val _playButtonImage = MutableLiveData<Int>()
    val playButtonImage: LiveData<Int> get() = _playButtonImage

    private val _playTextTime = MutableLiveData<String>()
    val playTextTime: LiveData<String> get() = _playTextTime

    private var mainThreadHandler = Handler(Looper.getMainLooper())

    init {
        _playButtonEnabled.value = false
        conditionPlayButton()
    }

    fun prepareTrack(url: String) {
        if (player.getPlayerState() == STATE_DEFAULT) {
            player.prepareTrack(url)
        }
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
        _playButtonImage.value = R.drawable.ic_pause
        updateTimeAndButton()
    }

    fun pausePlayer() {
        player.pausePlayer()
        _playButtonImage.value = R.drawable.ic_play
        mainThreadHandler.removeCallbacksAndMessages(null)
    }

    private fun updateTimeAndButton() {
        var lastCurrentTime = REFRESH_PLAY_TIME_MILLIS
        mainThreadHandler.postDelayed(
            object : Runnable {
                override fun run() {
                    val currentTime = player.getCurrentTime()
                    if (currentTime < REFRESH_PLAY_TIME_MILLIS && lastCurrentTime != currentTime) {
                        lastCurrentTime = currentTime
                        _playTextTime.value = millisToStrFormat(currentTime)
                    } else {
                        lastCurrentTime = REFRESH_PLAY_TIME_MILLIS
                        _playTextTime.value = millisToStrFormat(START_PLAY_TIME_MILLIS)
                        _playButtonImage.value = R.drawable.ic_play
                    }
                    // И снова планируем то же действие через 0.5 сек
                    mainThreadHandler.postDelayed(
                        this,
                        DELAY_MILLIS
                    )
                }
            }, DELAY_MILLIS
        )
    }

    private fun conditionPlayButton() {
        mainThreadHandler.postDelayed(
            object : Runnable {
                override fun run() {
                    _playButtonEnabled.value = player.getPlayerState() != STATE_DEFAULT
                    mainThreadHandler.postDelayed(this, DELAY_MILLIS)
                }
            }, DELAY_MILLIS)
    }

    override fun onCleared() {
        super.onCleared()
        mainThreadHandler.removeCallbacksAndMessages(null)
        player.releasePlayer()
    }
}