package com.practicum.playlistmaker.data.player.impl

import android.media.MediaPlayer
import com.practicum.playlistmaker.data.convertors.TrackDbConvertor
import com.practicum.playlistmaker.data.db.AppDatabase
import com.practicum.playlistmaker.data.favorites.db.entity.TrackEntity
import com.practicum.playlistmaker.domain.STATE_DEFAULT
import com.practicum.playlistmaker.domain.STATE_PAUSED
import com.practicum.playlistmaker.domain.STATE_PLAYING
import com.practicum.playlistmaker.domain.STATE_PREPARED
import com.practicum.playlistmaker.domain.player.PlayerRepository
import com.practicum.playlistmaker.domain.player.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.flow

class PlayerRepositoryImpl(
    private val mediaPlayer: MediaPlayer,
    private val appDatabase: AppDatabase,
    private val trackDbConvertor: TrackDbConvertor,
) : PlayerRepository {

    private var playerState = STATE_DEFAULT

    init {
        mediaPlayer.setOnPreparedListener {
            playerState = STATE_PREPARED
        }
        mediaPlayer.setOnCompletionListener {
            playerState = STATE_PREPARED
        }

    }

    override fun prepareTrack(url: String) {
        mediaPlayer.setDataSource(url)
        mediaPlayer.prepareAsync()
    }

    override fun startPlayer() {
        mediaPlayer.start()
        playerState = STATE_PLAYING
    }

    override fun pausePlayer() {
        mediaPlayer.pause()
        playerState = STATE_PAUSED
    }

    override fun releasePlayer() {
        playerState = STATE_DEFAULT
        mediaPlayer.release()
    }

    override fun getPlayerState(): Int {
        return playerState
    }

    override fun getCurrentTime(): Int {
        return mediaPlayer.currentPosition
    }

    override suspend fun insertTrackToFavorites(track: Track) {
        appDatabase
            .favoritesDao()
            .insertTrackEntity(convertToTrackEntity(track))
    }

    override suspend fun deleteTrackEntity(track: Track) {
        appDatabase
            .favoritesDao()
            .deleteTrackEntity(convertToTrackEntity(track))
    }

    override fun getFavoritesIdList(): Flow<List<Int>> {
        return flow {
            val id = appDatabase
                .favoritesDao()
                .getFavoritesIdList()
            emit(id)
        }
    }

    private fun convertToTrackEntity(track: Track): TrackEntity {
        return trackDbConvertor.map(track)
    }
}