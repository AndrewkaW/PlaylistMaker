package com.practicum.playlistmaker.domain.playlists.model

import java.io.Serializable

data class Playlist(
    val id: Int?,
    val name: String,
    val description: String,
    val pictureName: String?,
    val idsList: List<Int>,
    val numbersOfTrack: Int
) : Serializable
