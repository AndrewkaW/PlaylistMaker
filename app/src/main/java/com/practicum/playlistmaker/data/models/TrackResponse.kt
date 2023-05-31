package com.practicum.playlistmaker.data.models

import com.practicum.playlistmaker.Track

data class TrackResponse(val resultCount: Int, val results: List<Track>)
