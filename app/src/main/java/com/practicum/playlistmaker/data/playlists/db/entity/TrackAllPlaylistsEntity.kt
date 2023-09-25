package com.practicum.playlistmaker.data.playlists.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "tracks_all_playlists")
data class TrackAllPlaylistsEntity(
    @PrimaryKey
    val trackId: Int, //уникальный номемр трека
    val trackName: String, // Название композиции
    val artistName: String, // Имя исполнителя
    val trackTimeMillis: Int, // Продолжительность трека
    val artworkUrl100: String, // Ссылка на изображение обложки
    val collectionName: String, // Название альбома
    val releaseDate: String, // Год релиза трека
    val primaryGenreName: String, // Жанр трека
    val country: String, // Страна исполнителя
    val previewUrl: String, // Ссылка на отрывок трека
    val isFavorite: Boolean  // Избранный ли трек
)
