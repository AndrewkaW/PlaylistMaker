package com.practicum.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.data.favorites.db.dao.FavoritesDao
import com.practicum.playlistmaker.data.playlists.db.dao.PlaylistsDao
import com.practicum.playlistmaker.data.playlists.db.entity.PlaylistEntity
import com.practicum.playlistmaker.data.playlists.db.entity.TrackAllPlaylistsEntity
import com.practicum.playlistmaker.data.favorites.db.entity.TrackEntity

@Database(version = 4, entities = [TrackEntity::class, PlaylistEntity::class, TrackAllPlaylistsEntity::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoritesDao(): FavoritesDao

    abstract fun playlistsDao(): PlaylistsDao
}