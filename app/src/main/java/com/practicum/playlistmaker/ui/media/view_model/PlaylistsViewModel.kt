package com.practicum.playlistmaker.ui.media.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.practicum.playlistmaker.domain.playlists.PlaylistsInteractor
import com.practicum.playlistmaker.domain.playlists.model.Playlist
import com.practicum.playlistmaker.ui.media.PlaylistsState
import kotlinx.coroutines.launch

class PlaylistsViewModel(
    private val playlistsInteractor: PlaylistsInteractor
) : ViewModel() {

    private val _stateLiveData = MutableLiveData<PlaylistsState>()
    val stateLiveData: LiveData<PlaylistsState> = _stateLiveData

    fun fillData() {
        renderState(PlaylistsState.Loading)
        viewModelScope.launch {
            playlistsInteractor
                .getAllPlaylist()
                .collect { playlist ->
                    processResult(playlist)
                }
        }
    }

    private fun processResult(playlists: List<Playlist>) {
        if (playlists.isEmpty()) {
            renderState(PlaylistsState.Empty)
        } else {
            renderState(PlaylistsState.Content(playlists))
        }
    }

    private fun renderState(state: PlaylistsState) {
        _stateLiveData.postValue(state)
    }
}