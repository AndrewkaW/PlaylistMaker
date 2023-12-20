package com.practicum.playlistmaker.data.playlists.db.dao

import androidx.room.*
import com.practicum.playlistmaker.data.playlists.db.entity.PlaylistEntity
import com.practicum.playlistmaker.data.playlists.db.entity.TrackAllPlaylistsEntity

@Dao
interface PlaylistsDao {
    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addPlaylistEntity(playlistEntity: PlaylistEntity)

    @Query("SELECT * FROM playlist_table")
    suspend fun getAllPlaylist(): List<PlaylistEntity>

    @Update(entity = PlaylistEntity::class)
    suspend fun updatePlaylist(playlist: PlaylistEntity)

    @Insert(entity = TrackAllPlaylistsEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun addTrackToPlaylists(track: TrackAllPlaylistsEntity)

    @Query("SELECT * FROM tracks_all_playlists WHERE trackId = :trackId")
    suspend fun getTrackById(trackId: Int): TrackAllPlaylistsEntity

    @Query("DELETE FROM tracks_all_playlists WHERE trackId = :trackId")
    suspend fun deleteTrackFromAllPlaylistById(trackId: Int)

    @Query("SELECT * FROM playlist_table WHERE id = :id")
    suspend fun getPlaylistById(id: Int): PlaylistEntity

    @Insert(entity = PlaylistEntity::class, onConflict = OnConflictStrategy.REPLACE)
    suspend fun replacePlaylistEntity(playlistEntity: PlaylistEntity)

    @Query("DELETE FROM playlist_table WHERE id = :id")
    suspend fun deletePlaylistById(id: Int)

    @Delete(entity = PlaylistEntity::class)
    suspend fun deletePlaylistEntity(playlist: PlaylistEntity)
}