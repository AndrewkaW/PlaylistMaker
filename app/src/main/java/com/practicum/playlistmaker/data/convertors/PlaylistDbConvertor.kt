package com.practicum.playlistmaker.data.convertors

import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.data.playlists.db.entity.PlaylistEntity
import com.practicum.playlistmaker.data.playlists.db.entity.TrackAllPlaylistsEntity
import com.practicum.playlistmaker.domain.player.model.Track
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

    fun map(track: Track): TrackAllPlaylistsEntity {
        return TrackAllPlaylistsEntity(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.isFavorite,
        )
    }

    fun map(track: TrackAllPlaylistsEntity): Track {
        return Track(
            track.trackId,
            track.trackName,
            track.artistName,
            track.trackTimeMillis,
            track.artworkUrl100,
            track.collectionName,
            track.releaseDate,
            track.primaryGenreName,
            track.country,
            track.previewUrl,
            track.isFavorite,
        )
    }
}