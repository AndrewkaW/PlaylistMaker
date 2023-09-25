package com.practicum.playlistmaker.ui.player.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.*
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.utils.DateUtils.millisToStrFormat
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(private val player: PlayerInteractor) : ViewModel() {

    private val _playButtonEnabled = MutableLiveData<Boolean>()
    val playButtonEnabled: LiveData<Boolean> get() = _playButtonEnabled

    private val _playButtonImage = MutableLiveData<Int>()
    val playButtonImage: LiveData<Int> get() = _playButtonImage

    private val _favoriteButton = MutableLiveData<Boolean>()
    val favoriteButton: LiveData<Boolean> get() = _favoriteButton

    private val _playTextTime = MutableLiveData<String>()
    val playTextTime: LiveData<String> get() = _playTextTime

    private var timerJob: Job? = null

    private lateinit var track: Track

    private fun trackIsFavorite() {
        viewModelScope.launch {
            player
                .trackIsFavorite(track)
                .collect { isFavorite -> _favoriteButton.value = isFavorite }
        }
    }

    fun favoriteButtonFunction() {
        when (track.isFavorite) {
            true -> deleteFavoriteTrack()
            false -> addTrackToFavorites()
        }
    }

    private fun deleteFavoriteTrack() {
        track = track.copy(isFavorite = false)
        viewModelScope.launch {
            player.deleteFavoriteTrack(track)
        }
        _favoriteButton.value = false
    }

    private fun addTrackToFavorites() {
        track = track.copy(isFavorite = true)
        viewModelScope.launch {
            player.addTrackToFavorites(track)
        }
        _favoriteButton.value = true
    }

    fun prepareTrack(track: Track) {
        this.track = track
        trackIsFavorite()
        if (player.getPlayerState() == STATE_DEFAULT) {
            player.prepareTrack(track.previewUrl)
        }
    }

    fun playbackControl() {
        when (player.getPlayerState()) {
            STATE_PLAYING -> {
                pausePlayer()
            }
            STATE_PREPARED -> {
                startPlayer()
            }
            STATE_PAUSED -> {
                startPlayer()
            }
            else -> {}
        }
    }

    private fun startPlayer() {
        player.startPlayer()
        _playButtonImage.value = R.drawable.ic_pause
        startTimer()
    }

    fun pausePlayer() {
        player.pausePlayer()
        _playButtonImage.value = R.drawable.ic_play
        timerJob?.cancel()
    }

    override fun onCleared() {
        super.onCleared()
        player.releasePlayer()
    }

    private fun startTimer() {
        timerJob = viewModelScope.launch {
            while (player.getPlayerState() == STATE_PLAYING) {
                delay(DELAY_MILLIS)
                _playTextTime.value = millisToStrFormat(player.getCurrentTime())
            }
            _playTextTime.value = millisToStrFormat(START_PLAY_TIME_MILLIS)
            _playButtonImage.value = R.drawable.ic_play
        }
    }
}