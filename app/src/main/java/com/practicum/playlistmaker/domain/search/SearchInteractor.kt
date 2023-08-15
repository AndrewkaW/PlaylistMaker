package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.player.model.Track
import kotlinx.coroutines.flow.Flow

interface SearchInteractor {
    fun searchTrackList(expression: String): Flow<Pair<List<Track>?, String?>>
    fun clearHistoryList()
    fun getHistoryList(): ArrayList<Track>
    fun addTrackToHistory(track: Track)
}