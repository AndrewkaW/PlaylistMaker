package com.practicum.playlistmaker.data

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.domain.playlists.model.Playlist

class PlaylistDbConvertor(private val gson: Gson) {
    fun map(playlist: Playlist): PlaylistEntity {
        return PlaylistEntity(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.pictureName,
            gson.toJson(playlist.idsList),
            playlist.numbersOfTrack
        )
    }

    fun map(playlist: PlaylistEntity): Playlist {
        return Playlist(
            playlist.id,
            playlist.name,
            playlist.description,
            playlist.pictureName,
            idsList = gson.fromJson(playlist.idsListGson, object : TypeToken<List<Int>>() {}.type),
            playlist.numbersOfTrack
        )
    }
}