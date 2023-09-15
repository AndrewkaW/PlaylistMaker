package com.practicum.playlistmaker.data.db.dao

import androidx.room.*
import com.practicum.playlistmaker.data.db.entity.PlaylistEntity

@Dao
interface PlaylistsDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPlaylistEntity(playlistEntity: PlaylistEntity)

    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylistEntity(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getAllPlaylist(): List<PlaylistEntity>

}