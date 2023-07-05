package com.practicum.playlistmaker.data.search

import com.practicum.playlistmaker.data.search.network.model.Response

interface NetworkClient {
    fun doRequest(text: String): Response
}