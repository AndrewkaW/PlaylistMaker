package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.utils.Resource

interface SearchRepository {
    fun searchTrackList(expression: String): Resource<List<Track>>
    fun clearHistoryList()
    fun getHistoryList(): ArrayList<Track>
    fun addTrackToHistory(track: Track)
}