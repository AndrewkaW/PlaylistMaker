package com.practicum.playlistmaker.data.favorites.db

import androidx.room.Database
import androidx.room.RoomDatabase
import com.practicum.playlistmaker.data.favorites.db.dao.FavoritesDao
import com.practicum.playlistmaker.data.favorites.db.entity.TrackEntity

@Database(version = 1, entities = [TrackEntity::class])
abstract class AppDatabase : RoomDatabase(){

    abstract fun favoritesDao(): FavoritesDao
}