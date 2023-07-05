package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.Track

interface HistoryStorage {
    fun clearList()
    fun getList(): ArrayList<Track>
    fun addTrack(track: Track)
}