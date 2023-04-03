package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.apple.ItunesApi
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import retrofit2.Retrofit
import retrofit2.converter.gson.GsonConverterFactory

class SearchActivity : AppCompatActivity() {

    companion object{
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }

    private var searchEditText : String = ""

    private val itunesBaseUrl = "https://itunes.apple.com"
    private val retrofit = Retrofit.Builder()
        .baseUrl(itunesBaseUrl)
        .addConverterFactory(GsonConverterFactory.create())
        .build()
    private val itunesService = retrofit.create(ItunesApi::class.java)

    private val searchTrackList = ArrayList<Track>()

    private lateinit var inputEditText  : EditText
    private lateinit var clearButton : ImageView
    private val searchTextWatcher = object  : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            clearButton.visibility = clearButtonVisibility(s)
            searchEditText = s.toString()
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

//        searchTrackList.addAll(listOf(
//            Track("Smells Like Teen Spirit","Nirvana",27000,
//                "https://is5-ssl.mzstatic.com/image/thumb/Music115/v4/7b/58/c2/7b58c21a-2b51-2bb2-e59a-9bb9b96ad8c3/00602567924166.rgb.jpg/100x100bb.jpg"),
//            Track("Billie Jean","Michael Jackson",31000,
//                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/3d/9d/38/3d9d3811-71f0-3a0e-1ada-3004e56ff852/827969428726.jpg/100x100bb.jpg"),
//            Track("Stayin' Alive","Bee Gees",23000,
//                "https://is4-ssl.mzstatic.com/image/thumb/Music115/v4/1f/80/1f/1f801fc1-8c0f-ea3e-d3e5-387c6619619e/16UMGIM86640.rgb.jpg/100x100bb.jpg"),
//            Track("Whole Lotta Love","Led Zeppelin",28000,
//                "https://is2-ssl.mzstatic.com/image/thumb/Music62/v4/7e/17/e3/7e17e33f-2efa-2a36-e916-7f808576cf6b/mzm.fyigqcbs.jpg/100x100bb.jpg"),
//            Track("Sweet Child O'Mine","Guns N' Roses",29000,
//                "https://is5-ssl.mzstatic.com/image/thumb/Music125/v4/a0/4d/c4/a04dc484-03cc-02aa-fa82-5334fcb4bc16/18UMGIM24878.rgb.jpg/100x100bb.jpg")
//        ))

        val recyclerViewTrack = findViewById<RecyclerView>(R.id.trackSearchRecycler)
        recyclerViewTrack.layoutManager = LinearLayoutManager(this)
        val trackAdapter = TracksAdapter(searchTrackList)
        recyclerViewTrack.adapter = trackAdapter

        inputEditText = findViewById(R.id.inputEditText)
        inputEditText.addTextChangedListener(searchTextWatcher)
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                if(inputEditText.text.isNotEmpty()){
                    itunesService.search(inputEditText.text.toString()).enqueue(object : Callback<TrackResponse> {
                        override fun onResponse(
                            call: Call<TrackResponse>,
                            response: Response<TrackResponse>
                        ) {
                            if (response.code() == 200) {
                                searchTrackList.clear()
                                if (response.body()?.results?.isNotEmpty() == true) {
                                    searchTrackList.addAll(response.body()?.results!!)
                                    trackAdapter.notifyDataSetChanged()
                                }
                                if (response.body()?.resultCount == 0) {
                                    //показать плейсхолдер с ошибкой что ничего не найдено
                                } else {
                                    // пустая заглушка
                                }
                            } else {
                                //показать плейсхолдер с ошибкой
                            }
                        }

                        override fun onFailure(call: Call<TrackResponse>, t: Throwable) {
                            //показывать плейсхолдер с ошибкой
                        }
                    })
                }
                true
            }
            false
        }

        clearButton = findViewById(R.id.clearIcon)
        clearButton.visibility = clearButtonVisibility(inputEditText.text)
        clearButton.setOnClickListener{
            clearSearch()
        }

        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_id).setNavigationOnClickListener {
            finish()
        }

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

        val view = this.currentFocus
        if (view != null) {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }
    }
}