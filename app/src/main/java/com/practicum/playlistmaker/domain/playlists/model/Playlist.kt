package com.practicum.playlistmaker.domain.playlists.model


data class Playlist(
    val id: Int?,
    val name: String,
    val description: String,
    val pictureName: String?,
    val idsList: List<Int>,
    val numbersOfTrack: Int
)
