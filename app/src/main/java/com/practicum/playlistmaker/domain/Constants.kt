package com.practicum.playlistmaker.domain

class Constants {
    companion object {
        const val STATE_DEFAULT = 0
        const val STATE_PREPARED = 1
        const val STATE_PLAYING = 2
        const val STATE_PAUSED = 3
        const val DELAY_MILLIS = 100L
        const val REFRESH_PLAY_TIME_MILLIS = 29900L
        const val START_PLAY_TIME_MILLIS = 0

        const val APP_SETTINGS = "APP_SETTINGS"
        const val DARK_THEME = "DARK_THEME"

        const val CLICK_ITEM_DELAY = 1000L

    }
}