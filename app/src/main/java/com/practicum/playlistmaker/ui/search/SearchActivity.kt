package com.practicum.playlistmaker.ui.search

import android.content.Intent
import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.google.gson.Gson
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.SearchHistory
import com.practicum.playlistmaker.Track
import com.practicum.playlistmaker.data.network.ItunesApi
import com.practicum.playlistmaker.data.dto.TrackResponse
import com.practicum.playlistmaker.domain.Constants.Companion.APP_SETTINGS
import com.practicum.playlistmaker.ui.player.activity.PlayerActivity
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    companion object {
        const val SEARCH_TEXT = "SEARCH_TEXT"
        const val TRACK = "TRACK"
        private const val CLICK_ITEM_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }

    enum class StateType {
        CONNECTION_ERROR, NOT_FOUND, SEARCH_RESULT, HISTORY_LIST, SEARCH_PROGRESS
    }

    private var searchEditText: String = ""

    private val itunesBaseUrl = "http://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(ItunesApi::class.java)

    private val searchTrackList = ArrayList<Track>()
    private val trackAdapter = TracksAdapter { clickOnTrack(it) }
    private val historyTrackAdapter = TracksAdapter { clickOnTrack(it) }

    private var isClickAllowed = true
    private val handler = Handler(Looper.getMainLooper())

    private val searchRunnable = Runnable { searchTrackList() }

    private lateinit var searchHistory: SearchHistory

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

    private val searchTextWatcher = object : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            clearButton.visibility = clearButtonVisibility(s)
            searchEditText = s.toString()
            if (inputEditText.hasFocus() && s.isNullOrEmpty() && searchHistory.getList()
                    .isNotEmpty()
            ) showState(StateType.HISTORY_LIST)
            else searchDebounce()
            Log.i("qwe1", "текст поиска - $searchEditText")
        }

        override fun afterTextChanged(s: Editable?) {}
    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchEditText)
        Log.i("qwe1", "текст поиска при сохранении - $searchEditText")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchEditText = savedInstanceState.getString(SEARCH_TEXT).toString()
        Log.i("qwe1", "текст поиска при перезапуске - $searchEditText")
        inputEditText.setText(searchEditText)

    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)

        progressBar = findViewById(R.id.progressBar)

        recyclerViewTrack = findViewById(R.id.trackSearchRecycler)
        recyclerViewTrack.layoutManager = LinearLayoutManager(this)

        searchHistory = SearchHistory(getSharedPreferences(APP_SETTINGS, MODE_PRIVATE))

        inputEditText = findViewById(R.id.inputEditText)
        inputEditText.addTextChangedListener(searchTextWatcher)
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                searchTrackList()
                true
            }
            false
        } // производит поиск при нажатии на кнопку DONE на клавиатуре

        inputEditText.setOnFocusChangeListener { view, hasFocus ->
            if (searchHistory.getList().isNotEmpty() && hasFocus) showState(StateType.HISTORY_LIST)
        } //показывает историю если она не пустая а поиск в фокусе

        clearButton = findViewById(R.id.clearIcon)
        clearButton.visibility = clearButtonVisibility(inputEditText.text)
        clearButton.setOnClickListener {
            clearSearch()
        } // реализация кнопки очисти поисковой стоки

        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_id).setNavigationOnClickListener {
            finish()
        } // реализация кнопки назад
        refreshButtPh = findViewById(R.id.refresh_butt)
        refreshButtPh.setOnClickListener {
            searchTrackList()
        } // реализация кнопки обновить на окне с ошибкой соединения

        errorIcPh = findViewById(R.id.error_ic_ph)
        errorTextPh = findViewById(R.id.error_text_ph)
        errorPh = findViewById(R.id.error_ph)

        clearHistoryButton = findViewById(R.id.clear_history_butt)
        titleHistory = findViewById(R.id.history_title)

        clearHistoryButton.setOnClickListener {
            searchHistory.clearList()
            showState(StateType.SEARCH_RESULT)
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
        searchTrackList.clear()
        trackAdapter.notifyDataSetChanged()
        val view = this.currentFocus
        if (view != null) {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }
    }

    private fun showState(stateType: StateType) {
        when (stateType) {
            StateType.CONNECTION_ERROR -> {
                recyclerViewTrack.visibility = View.GONE
                errorPh.visibility = View.VISIBLE
                refreshButtPh.visibility = View.VISIBLE
                errorIcPh.setImageResource(R.drawable.ic_no_connection)
                errorTextPh.setText(R.string.ph_no_connection)
                titleHistory.visibility = View.GONE
                clearHistoryButton.visibility = View.GONE
                progressBar.visibility = View.GONE
            }
            StateType.NOT_FOUND -> {
                recyclerViewTrack.visibility = View.GONE
                errorPh.visibility = View.VISIBLE
                refreshButtPh.visibility = View.GONE
                errorIcPh.setImageResource(R.drawable.ic_not_found)
                errorTextPh.setText(R.string.ph_not_found)
                titleHistory.visibility = View.GONE
                clearHistoryButton.visibility = View.GONE
                progressBar.visibility = View.GONE
            }
            StateType.SEARCH_PROGRESS -> {
                recyclerViewTrack.visibility = View.GONE
                errorPh.visibility = View.GONE
                refreshButtPh.visibility = View.GONE
                titleHistory.visibility = View.GONE
                clearHistoryButton.visibility = View.GONE
                progressBar.visibility = View.VISIBLE
            }
            StateType.SEARCH_RESULT -> {
                trackAdapter.tracks = searchTrackList
                recyclerViewTrack.adapter = trackAdapter
                recyclerViewTrack.visibility = View.VISIBLE
                errorPh.visibility = View.GONE
                refreshButtPh.visibility = View.GONE
                titleHistory.visibility = View.GONE
                clearHistoryButton.visibility = View.GONE
                progressBar.visibility = View.GONE
            }
            StateType.HISTORY_LIST -> {
                historyTrackAdapter.tracks = searchHistory.getList()
                recyclerViewTrack.adapter = historyTrackAdapter
                recyclerViewTrack.visibility = View.VISIBLE
                errorPh.visibility = View.GONE
                refreshButtPh.visibility = View.GONE
                titleHistory.visibility = View.VISIBLE
                clearHistoryButton.visibility = View.VISIBLE
                progressBar.visibility = View.GONE
            }
        }

    }

    private fun searchTrackList() {
        if (inputEditText.text.isNotEmpty()) {
            showState(StateType.SEARCH_PROGRESS)
            itunesService.search(inputEditText.text.toString())
                .enqueue(object : Callback<TrackResponse> {
                    override fun onResponse(
                        call: Call<TrackResponse>,
                        response: Response<TrackResponse>
                    ) {
                        if (response.code() == 200) {
                            searchTrackList.clear()
                            if (response.body()?.results?.isNotEmpty() == true) {
                                searchTrackList.addAll(response.body()?.results!!)
                                trackAdapter.notifyDataSetChanged()
                                showState(StateType.SEARCH_RESULT)
                            } else showState(StateType.NOT_FOUND)
                        } else showState(StateType.CONNECTION_ERROR)
                    }

                    override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                        showState(StateType.CONNECTION_ERROR) //показывать плейсхолдер с ошибкой
                    }
                })
        }
    }

    private fun clickOnTrack(track: Track) {
        if (clickDebounce()) {
            searchHistory.addTrack(track)
            val playerIntent = Intent(this, PlayerActivity::class.java)
            playerIntent.putExtra(TRACK, Gson().toJson(track))
            startActivity(playerIntent)
        }
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            handler.postDelayed({ isClickAllowed = true }, CLICK_ITEM_DELAY)
        }
        return current
    }

    private fun searchDebounce() {
        handler.removeCallbacks(searchRunnable)
        handler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }
}