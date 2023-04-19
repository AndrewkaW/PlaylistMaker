package com.practicum.playlistmaker

import android.content.SharedPreferences
import com.google.gson.Gson
import com.google.gson.reflect.TypeToken

class SearchHistory(val sharedPreferences: SharedPreferences) {

    companion object{
        const val HISTORY_LIST = "HISTORY_LIST"
    }

    private fun saveList (historyListTrack: ArrayList<Track>){
        val jSON = Gson().toJson(historyListTrack)
        sharedPreferences.edit()
            .putString(HISTORY_LIST,jSON)
            .apply()
    }

    fun clearList() = sharedPreferences.edit()
            .remove(HISTORY_LIST)
            .apply()

    fun getList():ArrayList<Track> {
        val jSON = sharedPreferences.getString(HISTORY_LIST,"")
        return if (jSON.isNullOrBlank()) {
            arrayListOf()
        } else {
             Gson().fromJson(jSON, object : TypeToken<ArrayList<Track>>() {}.type)
        }
    }

    fun addTrack(track: Track){
        val historyList = getList()
        historyList.remove(track)
        historyList.add(0,track)
        if (historyList.size>10) historyList.removeLast()
        saveList(historyList)
    }
}