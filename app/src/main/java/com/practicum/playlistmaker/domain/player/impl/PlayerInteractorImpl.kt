package com.practicum.playlistmaker.domain.player.impl

import com.practicum.playlistmaker.domain.player.PlayerInteractor
import com.practicum.playlistmaker.domain.player.PlayerRepository

class PlayerInteractorImpl(private val playerRepository: PlayerRepository) :
    PlayerInteractor {

    override fun prepareTrack(url:String){
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

}