package com.practicum.playlistmaker.ui.search.activity

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.Track
import com.practicum.playlistmaker.databinding.ActivitySearchBinding
import com.practicum.playlistmaker.ui.player.activity.PlayerActivity
import com.practicum.playlistmaker.ui.search.adapter.TracksAdapter
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModelFactory
import com.practicum.playlistmaker.ui.search.view_model.model.SearchState
import com.practicum.playlistmaker.utils.Resource

class SearchActivity : AppCompatActivity() {

    private lateinit var binding: ActivitySearchBinding
    private lateinit var vmSearch: SearchViewModel

    private val trackAdapter = TracksAdapter { clickOnTrack(it) }
    private val historyTrackAdapter = TracksAdapter { clickOnTrack(it) }

    private lateinit var inputEditText: EditText
    private lateinit var clearButton: ImageView
    private lateinit var recyclerViewTrack: RecyclerView
    private lateinit var refreshButtPh: Button
    private lateinit var errorTextPh: TextView
    private lateinit var errorIcPh: ImageView
    private lateinit var errorPh: LinearLayout
    private lateinit var clearHistoryButton: Button
    private lateinit var titleHistory: TextView
    private lateinit var progressBar: ProgressBar

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater)
        setContentView(binding.root)

        vmSearch = ViewModelProvider(
            this,
            SearchViewModelFactory(this)
        )[SearchViewModel::class.java]

        vmSearch.stateLiveData.observe(this) {
            showState(it)
        }

        progressBar = binding.progressBar

        recyclerViewTrack = binding.trackSearchRecycler
        recyclerViewTrack.layoutManager = LinearLayoutManager(this)

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)

                if (inputEditText.hasFocus() && s.isNullOrEmpty()

                ) {
                    vmSearch.getHistoryList()
                }
                vmSearch.searchDebounce(
                    changedText = s?.toString() ?: ""
                )

            }

            override fun afterTextChanged(s: Editable?) {}
        }

        inputEditText = binding.inputEditText
        inputEditText.addTextChangedListener(searchTextWatcher)
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                vmSearch.searchTrackList(inputEditText.text.toString())
                true
            }
            false
        } // производит поиск при нажатии на кнопку DONE на клавиатуре

        clearButton = binding.clearIcon
        clearButton.visibility = clearButtonVisibility(inputEditText.text)
        clearButton.setOnClickListener {
            clearSearch()
        } // реализация кнопки очисти поисковой стоки

        binding.toolbarId.setNavigationOnClickListener {
            finish()
        } // реализация кнопки назад

        refreshButtPh = binding.refreshButt
        refreshButtPh.setOnClickListener {
            vmSearch.searchDebounce(inputEditText.text.toString())
        } // реализация кнопки обновить на окне с ошибкой соединения

        errorPh = binding.errorPh
        errorIcPh = binding.errorIcPh
        errorTextPh = binding.errorTextPh

        clearHistoryButton = binding.clearHistoryButt
        titleHistory = binding.historyTitle

        clearHistoryButton.setOnClickListener {
            vmSearch.clearHistory()
        } // реализация кнопки очистки истории

        inputEditText.requestFocus() // установка фокуса на поисковую строку
    }

    private fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    private fun clearSearch() {
        inputEditText.setText("")
        trackAdapter.notifyDataSetChanged()
        val view = this.currentFocus
        if (view != null) {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }
    }

    private fun showState(stateType: SearchState) {
        when (stateType) {
            is SearchState.Error -> {
                recyclerViewTrack.visibility = View.GONE
                errorPh.visibility = View.VISIBLE
                titleHistory.visibility = View.GONE
                clearHistoryButton.visibility = View.GONE
                progressBar.visibility = View.GONE
                if (stateType.errorMessage == Resource.CONNECTION_ERROR) {
                    errorIcPh.setImageResource(R.drawable.ic_no_connection)
                    errorTextPh.setText(R.string.ph_no_connection)
                    refreshButtPh.visibility = View.VISIBLE
                } else {
                    errorIcPh.setImageResource(R.drawable.ic_not_found)
                    errorTextPh.setText(R.string.ph_not_found)
                    refreshButtPh.visibility = View.GONE
                }
            }
            is SearchState.Loading -> {
                recyclerViewTrack.visibility = View.GONE
                errorPh.visibility = View.GONE
                refreshButtPh.visibility = View.GONE
                titleHistory.visibility = View.GONE
                clearHistoryButton.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
            is SearchState.SearchResult -> {
                trackAdapter.tracks = stateType.tracks
                recyclerViewTrack.adapter = trackAdapter
                trackAdapter.notifyDataSetChanged()
                recyclerViewTrack.visibility = View.VISIBLE
                errorPh.visibility = View.GONE
                refreshButtPh.visibility = View.GONE
                titleHistory.visibility = View.GONE
                clearHistoryButton.visibility = View.GONE
                progressBar.visibility = View.GONE
            }
            is SearchState.HistoryList -> {
                historyTrackAdapter.tracks = stateType.tracks
                recyclerViewTrack.adapter = historyTrackAdapter
                historyTrackAdapter.notifyDataSetChanged()
                recyclerViewTrack.visibility = View.VISIBLE
                errorPh.visibility = View.GONE
                refreshButtPh.visibility = View.GONE
                titleHistory.visibility = View.VISIBLE
                clearHistoryButton.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }

    }

    private fun clickOnTrack(track: Track) {
        if (vmSearch.clickDebounce()) {
            vmSearch.saveTrackToHistory(track)
            val playerIntent = Intent(this, PlayerActivity::class.java)
            playerIntent.putExtra(TRACK, Gson().toJson(track))
            startActivity(playerIntent)
        }
    }

    companion object {
        const val TRACK = "TRACK"
    }
}