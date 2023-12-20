package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.data.search.network.model.Response

interface NetworkClient {
    suspend fun doRequest(text: String): Response
}