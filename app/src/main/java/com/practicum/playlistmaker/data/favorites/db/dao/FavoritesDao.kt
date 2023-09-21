package com.practicum.playlistmaker.data.favorites.db.dao

import androidx.room.*
import com.practicum.playlistmaker.data.favorites.db.entity.TrackEntity

@Dao
interface FavoritesDao {
    @Insert(entity = TrackEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun insertTrackEntity(track: TrackEntity)

    @Delete(entity = TrackEntity::class)
    suspend fun deleteTrackEntity(track: TrackEntity)

    @Query("SELECT * FROM favorites_table")
    suspend fun getFavoritesList(): List<TrackEntity>

    @Query("SELECT trackId FROM favorites_table")
    suspend fun getFavoritesIdList(): List<Int>
}