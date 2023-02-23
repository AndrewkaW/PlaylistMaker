package com.practicum.playlistmaker

import androidx.appcompat.app.AppCompatActivity
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.util.Log
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import android.widget.ImageView

class SearchActivity : AppCompatActivity() {

    companion object{
        const val SEARCH_TEXT = "SEARCH_TEXT"
    }

    private var searchEditText : String = ""

    private lateinit var inputEditText  : EditText
    private lateinit var clearButton : ImageView
    private val searchTextWatcher = object  : TextWatcher {
        override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

        override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
            clearButton.visibility = clearButtonVisibility(s)
            searchEditText = s.toString()
            Log.i("qwe", "текст поиска при вставке - $searchEditText")
        }
        override fun afterTextChanged(s: Editable?) {}

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putString(SEARCH_TEXT, searchEditText)
        Log.i("qwe", "текст поиска при сохранении - $searchEditText")
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        searchEditText = savedInstanceState.getString(SEARCH_TEXT).toString()
        Log.i("qwe", "текст поиска при перезапуске - $searchEditText")
        inputEditText.setText(searchEditText)

    }
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)


        inputEditText = findViewById(R.id.inputEditText)
        inputEditText.addTextChangedListener(searchTextWatcher)

        clearButton = findViewById(R.id.clearIcon)
        clearButton.visibility = clearButtonVisibility(inputEditText.text)
        clearButton.setOnClickListener{
            clearSearch()
        }

        findViewById<androidx.appcompat.widget.Toolbar>(R.id.toolbar_id).setOnClickListener {
            finish()
        }

    }


    fun clearButtonVisibility(s: CharSequence?): Int {
        return if (s.isNullOrEmpty()) {
            View.GONE
        } else {
            View.VISIBLE
        }
    }

    fun clearSearch() {
        inputEditText.setText("")

        val view = this.currentFocus
        if (view != null) {
            val inputMethodManager = getSystemService(INPUT_METHOD_SERVICE) as InputMethodManager
            inputMethodManager.hideSoftInputFromWindow(view.windowToken, 0)
            view.clearFocus()
        }
    }
}