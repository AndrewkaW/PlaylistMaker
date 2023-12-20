package com.practicum.playlistmaker.utils

sealed class Resource<T>(val data: T? = null, val message: String? = null) {
    class Success<T>(data: T) : Resource<T>(data)
    class Error<T>(message: String, data: T? = null) : Resource<T>(data, message)

    companion object {
        const val NOT_FOUND = "NOT_FOUND"
        const val CONNECTION_ERROR = "CONNECTION_ERROR"
    }
}
