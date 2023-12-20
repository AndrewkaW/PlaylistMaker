package com.practicum.playlistmaker.ui.favorites.state

import com.practicum.playlistmaker.domain.player.model.Track

sealed interface FavoritesState {

    object Loading : FavoritesState

    data class Content(
        val tracks: List<Track>
    ) : FavoritesState

    object Empty : FavoritesState
}