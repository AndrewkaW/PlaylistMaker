package com.practicum.playlistmaker.data.search.network.model

data class TrackDto(
    val trackId: Int,
    val trackName: String,
    val artistName: String?,
    val trackTimeMillis: Int?,
    val artworkUrl100: String?,
    val artworkUrl60: String?,
    val collectionName: String?,
    val releaseDate: String?,
    val primaryGenreName: String?,
    val country: String?,
    val previewUrl: String?,
)