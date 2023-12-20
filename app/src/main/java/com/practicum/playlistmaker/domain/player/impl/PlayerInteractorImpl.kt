package com.practicum.playlistmaker.domain.player.impl

import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.PlayerRepository
import com.practicum.playlistmaker.domain.player.model.Track
import kotlinx.coroutines.flow.Flow
import kotlinx.coroutines.flow.map

class PlayerInteractorImpl(private val playerRepository: PlayerRepository) :
    PlayerInteractor {

    override fun prepareTrack(url: String) {
        playerRepository.prepareTrack(url)
    }

    override fun startPlayer() {
        playerRepository.startPlayer()
    }

    override fun pausePlayer() {
        playerRepository.pausePlayer()
    }

    override fun releasePlayer() {
        playerRepository.releasePlayer()
    }

    override fun getPlayerState(): Int {
        return playerRepository.getPlayerState()
    }

    override fun getCurrentTime(): Int {
        return playerRepository.getCurrentTime()
    }

    override fun trackIsFavorite(track: Track): Flow<Boolean> {
        return playerRepository.getFavoritesIdList()
            .map { list -> list.any { it == track.trackId } }
    }

    override suspend fun deleteFavoriteTrack(track: Track) {
        playerRepository.deleteTrackEntity(track)
    }

    override suspend fun addTrackToFavorites(track: Track) {
        playerRepository.insertTrackToFavorites(track)
    }
}