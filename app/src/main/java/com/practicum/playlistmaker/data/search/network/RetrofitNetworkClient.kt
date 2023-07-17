package com.practicum.playlistmaker.data.search.network

import android.content.Context
import android.net.ConnectivityManager
import android.net.NetworkCapabilities
import com.practicum.playlistmaker.data.search.NetworkClient
import com.practicum.playlistmaker.data.search.network.model.Response

class RetrofitNetworkClient(
    private val context: Context,
    private val itunesService: ItunesApi
) : NetworkClient {

    override fun doRequest(text: String): Response {
        if (!isConnected()) {
            return Response().apply { resultCode = -1 }
        }
        val response = itunesService.search(text).execute()
        val body = response.body()

        return body?.apply { resultCode = response.code() } ?: Response().apply {
            resultCode = response.code()
        }
    }

    private fun isConnected(): Boolean {
        val connectivityManager = context.getSystemService(
            Context.CONNECTIVITY_SERVICE
        ) as ConnectivityManager
        val capabilities =
            connectivityManager.getNetworkCapabilities(connectivityManager.activeNetwork)

        return capabilities != null &&
                (capabilities.hasTransport(NetworkCapabilities.TRANSPORT_CELLULAR) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_WIFI) ||
                capabilities.hasTransport(NetworkCapabilities.TRANSPORT_ETHERNET))
    }
}