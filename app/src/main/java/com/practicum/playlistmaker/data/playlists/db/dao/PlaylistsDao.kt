package com.practicum.playlistmaker.data.playlists.db.dao

import androidx.room.*
import com.practicum.playlistmaker.data.playlists.db.entity.PlaylistEntity
import com.practicum.playlistmaker.data.playlists.db.entity.TrackAllPlaylistsEntity

@Dao
interface PlaylistsDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.IGNORE)
    suspend fun addPlaylistEntity(playlistEntity: PlaylistEntity)

    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylistEntity(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getAllPlaylist(): List<PlaylistEntity>

    @Update (entity = PlaylistEntity::class)
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Insert(entity = TrackAllPlaylistsEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToPlaylists(track: TrackAllPlaylistsEntity)
}