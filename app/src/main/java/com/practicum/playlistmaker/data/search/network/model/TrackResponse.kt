package com.practicum.playlistmaker.data.search.network.model

import com.practicum.playlistmaker.domain.player.model.Track

data class TrackResponse(val resultCount: Int, val results: List<Track>) : Response()
