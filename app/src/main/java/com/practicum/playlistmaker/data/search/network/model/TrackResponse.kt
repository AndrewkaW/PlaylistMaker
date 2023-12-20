package com.practicum.playlistmaker.data.search.network.model


data class TrackResponse(val resultCount: Int, val results: List<TrackDto>) : Response()
