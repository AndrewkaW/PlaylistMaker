package com.practicum.playlistmaker.data.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.data.db.dao.FavoritesDao
import com.practicum.playlistmaker.data.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class], exportSchema = false)
abstract class AppDatabase : RoomDatabase() {

    abstract fun favoritesDao(): FavoritesDao
}