package com.practicum.playlistmaker.ui.search

import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.*
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.practicum.playlistmaker.R
import com.practicum.playlistmaker.databinding.FragmentSearchBinding
import com.practicum.playlistmaker.domain.player.model.Track
import com.practicum.playlistmaker.ui.search.adapter.TracksAdapter
import com.practicum.playlistmaker.ui.search.view_model.SearchViewModel
import com.practicum.playlistmaker.ui.search.view_model.model.SearchState
import com.practicum.playlistmaker.utils.Resource
import org.koin.androidx.viewmodel.ext.android.viewModel

class SearchFragment : Fragment() {

    private var _binding: FragmentSearchBinding? = null
    private val binding get() = _binding!!
    private val vmSearch: SearchViewModel by viewModel()

    private val trackAdapter = TracksAdapter({ clickOnTrack(it) })
    private val historyTrackAdapter = TracksAdapter({ clickOnTrack(it) })

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

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        _binding = FragmentSearchBinding.inflate(inflater, container, false)
        return binding.root
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        vmSearch.stateLiveData.observe(viewLifecycleOwner) {
            showState(it)
        }

        progressBar = binding.progressBar

        recyclerViewTrack = binding.trackSearchRecycler
        recyclerViewTrack.layoutManager = LinearLayoutManager(requireContext())

        inputEditText = binding.inputEditText

        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(s: CharSequence?, start: Int, count: Int, after: Int) {}

            override fun onTextChanged(s: CharSequence?, start: Int, before: Int, count: Int) {
                clearButton.visibility = clearButtonVisibility(s)
                if (inputEditText.hasFocus() && s.isNullOrEmpty()) {
                    vmSearch.getHistoryList()
                }
                vmSearch.searchDebounce(inputEditText.text.toString())
            }

            override fun afterTextChanged(s: Editable?) {}
        }

        inputEditText.addTextChangedListener(searchTextWatcher)
        inputEditText.setOnEditorActionListener { _, actionId, _ ->
            if (actionId == EditorInfo.IME_ACTION_DONE) {
                vmSearch.searchTrackList(inputEditText.text.toString())
                true
            }
            false
        }

        clearButton = binding.clearIcon
        clearButton.visibility = clearButtonVisibility(inputEditText.text)
        clearButton.setOnClickListener {
            clearSearch()
        }

        refreshButtPh = binding.refreshButt
        refreshButtPh.setOnClickListener {
            vmSearch.searchTrackList(inputEditText.text.toString())
        }

        errorPh = binding.errorPh
        errorIcPh = binding.errorIcPh
        errorTextPh = binding.errorTextPh

        clearHistoryButton = binding.clearHistoryButt
        titleHistory = binding.historyTitle

        clearHistoryButton.setOnClickListener {
            vmSearch.clearHistory()
        }

        inputEditText.requestFocus()
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
        val view = requireActivity().currentFocus
        if (view != null) {
            val inputMethodManager =
                requireContext().getSystemService(AppCompatActivity.INPUT_METHOD_SERVICE) as InputMethodManager
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
                historyTrackAdapter.notifyDataSetChanged()
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

    private fun clickOnTrack(track: Track) {
        if (vmSearch.clickDebounce()) {
            vmSearch.saveTrackToHistory(track)
            findNavController().navigate(
                R.id.action_searchFragment_to_playerFragment,
                Bundle().apply { putSerializable(TRACK, track) }
            )
        }
    }

    override fun onStart() {
        super.onStart()
        if (vmSearch.stateLiveData.value is SearchState.HistoryList)
            vmSearch.getHistoryList()
    }

    override fun onDestroyView() {
        super.onDestroyView()
        _binding = null
    }

    companion object {
        const val TRACK = "TRACK"
    }
}