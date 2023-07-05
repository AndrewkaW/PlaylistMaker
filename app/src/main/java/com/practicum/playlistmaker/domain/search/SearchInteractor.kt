package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.Track
import com.practicum.playlistmaker.utils.Resource

interface SearchInteractor {
    fun searchTrackList(expression: String, consumer: SearchConsumer)
    fun clearHistoryList()
    fun getHistoryList(): ArrayList<Track>
    fun addTrackToHistory(track: Track)

    interface SearchConsumer {
        fun consume(foundTracks: List<Track>?, errorMassage: String?)
    }
}