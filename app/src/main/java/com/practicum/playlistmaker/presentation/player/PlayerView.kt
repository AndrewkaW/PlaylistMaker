package com.practicum.playlistmaker.presentation.player

interface PlayerView {
    fun setTrackName(name: String)
    fun setArtistName(name: String)
    fun setTrackTime(time: String)
    fun setArtwork(url: String)
    fun setCollection(name: String)
    fun setCollectionVisibility(visible: Boolean)
    fun setReleaseDate(date: String)
    fun setPrimaryGenre(name: String)
    fun setCountry(name: String)
    fun setPlayButtonEnabled(isEnabled: Boolean)
    fun setPlayButtonImage(resId: Int)
    fun setPlayTimeText(time: String)
}