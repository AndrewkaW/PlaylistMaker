package com.practicum.playlistmaker.ui.player.view_model

import android.content.Context
import android.widget.Toast
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.domain.*
import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.domain.playlists.PlaylistsInteractor
import com.practicum.playlistmaker.domain.playlists.model.Playlist
import com.practicum.playlistmaker.utils.DateUtils.millisToStrFormat
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class PlayerViewModel(
    private val player: PlayerInteractor,
    private val playlistsInteractor: PlaylistsInteractor,
) : ViewModel() {

    private val _playButtonEnabled = MutableLiveData<Boolean>()
    val playButtonEnabled: LiveData<Boolean> get() = _playButtonEnabled

    private val _playButtonImage = MutableLiveData<Int>()
    val playButtonImage: LiveData<Int> get() = _playButtonImage

    private val _favoriteButton = MutableLiveData<Boolean>()
    val favoriteButton: LiveData<Boolean> get() = _favoriteButton

    private val _playTextTime = MutableLiveData<String>()
    val playTextTime: LiveData<String> get() = _playTextTime

    private val _playlists = MutableLiveData<List<Playlist>>()
    val playlists: LiveData<List<Playlist>> get() = _playlists

    private val _playlistPanelHide = MutableLiveData<Boolean>()
    val playlistPanelHide: LiveData<Boolean> get() = _playlistPanelHide

    private val _thereTrackInPlaylist = MutableLiveData<Boolean>()
    val thereTrackInPlaylist: LiveData<Boolean> get() = _thereTrackInPlaylist

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

    fun getPlaylists() {
        viewModelScope.launch {
            playlistsInteractor
                .getAllPlaylist()
                .collect { list ->
                    _playlists.value = list
                }
        }
    }

    fun addIdTrackToPlaylist(playlist: Playlist) {
        if (playlist.idsList.contains(track.trackId)) {
            _thereTrackInPlaylist.value = true
        } else {
            viewModelScope.launch {
                playlistsInteractor.addIdTrackToPlaylist(track, playlist)
            }
            _thereTrackInPlaylist.value = false
            _playlistPanelHide.value = true
        }
    }
}