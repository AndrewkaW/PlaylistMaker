package com.practicum.playlistmaker.ui.search.view_model

import android.content.Context
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.practicum.playlistmaker.creator.Creator

class SearchViewModelFactory(context: Context) : ViewModelProvider.Factory {

    private val searchInteractor = Creator.provideSearchInteractor(context)

    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return SearchViewModel(searchInteractor) as T
    }
}