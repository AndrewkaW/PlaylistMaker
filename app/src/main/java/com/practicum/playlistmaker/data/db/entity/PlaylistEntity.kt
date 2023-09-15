package com.practicum.playlistmaker.data.db.entity

import androidx.room.Entity
import androidx.room.PrimaryKey

@Entity(tableName = "playlist_table")
data class PlaylistEntity
    (
    @PrimaryKey(autoGenerate = true)
    val id: Int?,
    val name: String,
    val description: String,
    val pictureName: String?,
    val idsListGson: String,
    val numbersOfTrack: Int
)
