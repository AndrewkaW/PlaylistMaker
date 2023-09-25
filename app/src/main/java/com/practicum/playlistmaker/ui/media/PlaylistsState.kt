package com.practicum.playlistmaker.ui.media

import com.practicum.playlistmaker.domain.playlists.model.Playlist

sealed interface PlaylistsState {

    object Loading : PlaylistsState

    data class Content(
        val playlists: List<Playlist>
    ) : PlaylistsState

    object Empty : PlaylistsState
}