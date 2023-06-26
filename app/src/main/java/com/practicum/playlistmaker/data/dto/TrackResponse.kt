package com.practicum.playlistmaker.data.dto

import com.practicum.playlistmaker.Track

data class TrackResponse(val resultCount: Int, val results: List<Track>)
