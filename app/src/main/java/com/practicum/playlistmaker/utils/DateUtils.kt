package com.practicum.playlistmaker.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {
    fun millisToStrFormat(millis: Int) =
        SimpleDateFormat("mm:ss", Locale.getDefault()).format(millis)
}