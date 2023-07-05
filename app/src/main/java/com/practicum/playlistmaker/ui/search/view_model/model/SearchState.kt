package com.practicum.playlistmaker.ui.search.view_model.model

import com.practicum.playlistmaker.Track

sealed interface SearchState {

    object Loading : SearchState

    data class SearchResult(
        val tracks: ArrayList<Track>
    ) : SearchState

    data class HistoryList(
        val tracks: ArrayList<Track>
    ) : SearchState

    data class Error(
        val errorMessage: String
    ) : SearchState

}