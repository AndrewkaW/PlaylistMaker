package com.practicum.playlistmaker.domain.search

import com.practicum.playlistmaker.domain.player.model.Track

interface SearchInteractor {
    fun searchTrackList(expression: String, consumer: SearchConsumer)
    fun clearHistoryList()
    fun getHistoryList(): ArrayList<Track>
    fun addTrackToHistory(track: Track)

    interface SearchConsumer {
        fun consume(foundTracks: List<Track>?, errorMassage: String?)
    }
}