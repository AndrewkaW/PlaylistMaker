package com.practicum.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.data.db.dao.FavoritesDao
import com.practicum.playlistmaker.data.db.dao.PlaylistsDao
import com.practicum.playlistmaker.data.db.entity.PlaylistEntity
import com.practicum.playlistmaker.data.db.entity.TrackEntity

@Database(version = 3, entities = [TrackEntity::class, PlaylistEntity::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoritesDao(): FavoritesDao

    abstract fun playlistsDao(): PlaylistsDao
}