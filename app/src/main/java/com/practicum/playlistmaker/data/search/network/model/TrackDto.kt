package com.practicum.playlistmaker.data.search.network.model

data class TrackDto(
    val trackId: Int, // уникальный идентификатор трека
    val trackName: String, // Название композиции
    val artistName: String?, // Имя исполнителя
    val trackTimeMillis: Int?, // Продолжительность трека
    val artworkUrl100: String?,
    val artworkUrl60: String?, // Ссылка на изображение обложки
    val collectionName: String?, // Название альбома
    val releaseDate: String?, // Год релиза трека
    val primaryGenreName: String?, // Жанр трека
    val country: String?, // Страна исполнителя
    val previewUrl: String?, // Ссылка на отрывок
)
