package com.practicum.playlistmaker.utils

import java.text.SimpleDateFormat
import java.util.*

object DateUtils {

    private const val PLAY_TIME_FORMAT = "mm:ss"
    private const val YEAR_FORMAT = "yyyy"

    fun millisToStrFormat(millis: Int) =
        SimpleDateFormat(PLAY_TIME_FORMAT, Locale.getDefault()).format(millis)

    fun strDateFormat(date: String): String {
        val formatDate = SimpleDateFormat(YEAR_FORMAT, Locale.getDefault()).parse(date)
        return SimpleDateFormat(YEAR_FORMAT, Locale.getDefault()).format(formatDate!!)
    }

    fun previewUrlSizeChange(url: String): String {
        return url.replaceAfterLast('/', "512x512bb.jpg")
    }
}