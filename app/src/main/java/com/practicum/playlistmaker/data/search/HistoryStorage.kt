package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.domain.player.model.Track

interface HistoryStorage {
    fun clearList()
    fun getList(): ArrayList<Track>
    fun addTrack(track: Track)
}