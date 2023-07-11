package com.practicum.playlistmaker.data.search.impl

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.data.search.HistoryStorage

class HistoryStorageImpl(private val sharedPreferences: SharedPreferences) : HistoryStorage {

    private fun saveList(historyListTrack: ArrayList<Track>) {
        val jSON = Gson().toJson(historyListTrack)
        sharedPreferences.edit()
            .putString(HISTORY_LIST, jSON)
            .apply()
    }

    override fun clearList() = sharedPreferences.edit()
        .remove(HISTORY_LIST)
        .apply()

    override fun getList(): ArrayList<Track> {
        val jSON = sharedPreferences.getString(HISTORY_LIST, "")
        return if (jSON.isNullOrBlank()) {
            arrayListOf()
        } else {
            Gson().fromJson(jSON, object : TypeToken<ArrayList<Track>>() {}.type)
        }
    }

    override fun addTrack(track: Track) {
        val historyList = getList()
        historyList.remove(track)
        historyList.add(0, track)
        if (historyList.size > 10) historyList.removeLast()
        saveList(historyList)
    }

    companion object {
        const val HISTORY_LIST = "HISTORY_LIST"
    }
}