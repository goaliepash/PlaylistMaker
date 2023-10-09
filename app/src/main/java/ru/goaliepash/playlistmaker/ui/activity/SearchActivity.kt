package ru.goaliepash.playlistmaker.ui.activity

import android.annotation.SuppressLint
import android.content.Context
import android.content.Intent
import android.os.Bundle
import android.os.Handler
import android.os.Looper
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.lifecycle.ViewModelProvider
import androidx.recyclerview.widget.LinearLayoutManager
import ru.goaliepash.playlistmaker.R
import ru.goaliepash.playlistmaker.databinding.ActivitySearchBinding
import ru.goaliepash.playlistmaker.domain.model.Track
import ru.goaliepash.playlistmaker.presentation.state.TracksState
import ru.goaliepash.playlistmaker.presentation.view_model.SearchViewModel
import ru.goaliepash.playlistmaker.ui.adapter.TrackAdapter
import ru.goaliepash.playlistmaker.ui.listener.OnTrackClickListener

class SearchActivity : AppCompatActivity() {

    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {
        if (binding.editTextSearch.text.toString().isNotEmpty()) {
            viewModel.search(binding.editTextSearch.text.toString())
        }
    }
    private val onTrackClickListener = OnTrackClickListener { track -> onTrackClick(track) }

    private var isClickAllowed = true

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var searchHistoryTrackAdapter: TrackAdapter
    private lateinit var binding: ActivitySearchBinding
    private lateinit var viewModel: SearchViewModel

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivitySearchBinding.inflate(layoutInflater).also { setContentView(it.root) }
        viewModel = ViewModelProvider(this, SearchViewModel.getViewModelFactory(applicationContext))[SearchViewModel::class.java]
        viewModel.getTracksState().observe(this) { renderTracksState(it) }
        viewModel.getSearchHistoryTracks().observe(this) { renderSearchHistoryTracks(it) }
        initUI()
    }

    private fun initUI() {
        initImageViewBack()
        initEditTextSearch()
        initRecyclerViewTracks()
        initButtonPlaceholderRefresh()
        initSearchHistory()
    }

    private fun initImageViewBack() {
        binding.imageViewBack.setOnClickListener { finish() }
    }

    private fun initEditTextSearch() {
        binding.editTextSearch.setOnFocusChangeListener { _, b ->
            if (b && binding.editTextSearch.text.isEmpty()) {
                viewModel.getSearchHistory()
            } else {
                binding.linearLayoutSearchHistory.visibility = View.GONE
            }
        }
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (binding.editTextSearch.hasFocus() && p0?.isEmpty() == true) {
                    viewModel.getSearchHistory()
                } else {
                    binding.linearLayoutSearchHistory.visibility = View.GONE
                }
                if (p0.isNullOrEmpty()) {
                    setDrawableEndVisibility(false, binding.editTextSearch)
                } else {
                    setDrawableEndVisibility(true, binding.editTextSearch)
                }
                searchDebounce()
            }

            override fun afterTextChanged(p0: Editable?) {}
        }
        binding.editTextSearch.addTextChangedListener(searchTextWatcher)
        binding.editTextSearch.onDrawableEndClick { onEditTextSearchClearClick() }
        setDrawableEndVisibility(false, binding.editTextSearch)
    }

    private fun initRecyclerViewTracks() {
        trackAdapter = TrackAdapter(onTrackClickListener)
        binding.recyclerViewTracks.adapter = trackAdapter
        binding.recyclerViewTracks.layoutManager = LinearLayoutManager(this)
    }

    private fun initButtonPlaceholderRefresh() {
        binding.buttonPlaceholderRefresh.setOnClickListener { onButtonPlaceHolderRefresh() }
    }

    private fun initSearchHistory() {
        initRecyclerViewSearchHistory()
        initButtonClearHistory()
    }

    private fun initRecyclerViewSearchHistory() {
        searchHistoryTrackAdapter = TrackAdapter(onTrackClickListener)
        binding.recyclerViewSearchHistory.adapter = searchHistoryTrackAdapter
        binding.recyclerViewSearchHistory.layoutManager = LinearLayoutManager(this)
    }

    private fun initButtonClearHistory() {
        binding.buttonClearSearchHistory.setOnClickListener { viewModel.clearSearchHistory() }
    }

    private fun setDrawableEndVisibility(isVisible: Boolean, editText: EditText) {
        if (isVisible) {
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_search, 0, R.drawable.ic_close, 0)
        } else {
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0)
        }
    }

    private fun hideKeyboard(editText: EditText) {
        val inputMethodManager = getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun EditText.onDrawableEndClick(action: () -> Unit) {
        setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                view as EditText
                val end = if (view.resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL) view.left else right
                if (motionEvent.rawX >= (end - view.compoundPaddingEnd)) {
                    action.invoke()
                    return@setOnTouchListener true
                }
            }
            return@setOnTouchListener false
        }
    }

    private fun showTracks(tracks: List<Track>) {
        binding.recyclerViewTracks.visibility = View.VISIBLE
        binding.linearLayoutPlaceholderMessage.visibility = View.GONE
        binding.linearLayoutSearchHistory.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
        trackAdapter.tracks.clear()
        trackAdapter.tracks.addAll(tracks)
        trackAdapter.notifyDataSetChanged()
    }

    private fun showNothingWasFoundPlaceholder() {
        binding.recyclerViewTracks.visibility = View.GONE
        binding.linearLayoutPlaceholderMessage.visibility = View.VISIBLE
        binding.linearLayoutSearchHistory.visibility = View.GONE
        binding.imageViewPlaceholderMessage.setBackgroundResource(R.drawable.ic_search_error)
        binding.textViewPlaceholderMessage.text = getString(R.string.nothing_was_found)
        binding.buttonPlaceholderRefresh.visibility = View.GONE
        binding.progressBar.visibility = View.GONE
    }

    private fun showErrorMessagePlaceholder(@DrawableRes backgroundResource: Int, @StringRes text: Int) {
        binding.recyclerViewTracks.visibility = View.GONE
        binding.linearLayoutPlaceholderMessage.visibility = View.VISIBLE
        binding.linearLayoutSearchHistory.visibility = View.GONE
        binding.imageViewPlaceholderMessage.setBackgroundResource(backgroundResource)
        binding.textViewPlaceholderMessage.text = getString(text)
        binding.buttonPlaceholderRefresh.visibility = View.VISIBLE
        binding.progressBar.visibility = View.GONE
    }

    private fun onEditTextSearchClearClick() {
        binding.editTextSearch.setText("")
        setDrawableEndVisibility(false, binding.editTextSearch)
        hideKeyboard(binding.editTextSearch)
        trackAdapter.tracks.clear()
        trackAdapter.notifyDataSetChanged()
        binding.linearLayoutPlaceholderMessage.visibility = View.GONE
        viewModel.getSearchHistory()
    }

    private fun onTrackClick(track: Track) {
        if (clickDebounce()) {
            searchHistoryTrackAdapter.tracks.clear()
            viewModel.getSearchHistoryTracks().value?.let { searchHistoryTrackAdapter.tracks.addAll(it) }
            if (searchHistoryTrackAdapter.tracks.contains(track)) {
                searchHistoryTrackAdapter.tracks.remove(track)
            } else if (searchHistoryTrackAdapter.tracks.size == MAX_SIZE_OF_SEARCH_HISTORY_TRACKS) {
                searchHistoryTrackAdapter.tracks.removeAt(MAX_SIZE_OF_SEARCH_HISTORY_TRACKS - 1)
            }
            searchHistoryTrackAdapter.tracks.add(0, track)
            viewModel.addSearchHistory(searchHistoryTrackAdapter.tracks)
            openTrackActivity(track)
            searchHistoryTrackAdapter.notifyDataSetChanged()
        }
    }

    private fun openTrackActivity(track: Track) {
        Intent(this, AudioPlayerActivity::class.java).apply {
            putExtra(AudioPlayerActivity.EXTRA_TRACK, track)
            startActivity(this)
        }
    }

    private fun searchDebounce() {
        mainThreadHandler.removeCallbacks(searchRunnable)
        mainThreadHandler.postDelayed(searchRunnable, SEARCH_DEBOUNCE_DELAY)
    }

    private fun showLoading() {
        binding.recyclerViewTracks.visibility = View.GONE
        binding.linearLayoutPlaceholderMessage.visibility = View.GONE
        binding.linearLayoutSearchHistory.visibility = View.GONE
        binding.progressBar.visibility = View.VISIBLE
    }

    private fun onButtonPlaceHolderRefresh() {
        binding.linearLayoutPlaceholderMessage.visibility = View.GONE
        searchDebounce()
    }

    private fun clickDebounce(): Boolean {
        val current = isClickAllowed
        if (isClickAllowed) {
            isClickAllowed = false
            mainThreadHandler.postDelayed({ isClickAllowed = true }, CLICK_DEBOUNCE_DELAY)
        }
        return current
    }

    private fun renderTracksState(tracksState: TracksState) {
        when (tracksState) {
            is TracksState.Loading -> showLoading()
            is TracksState.Content -> showTracks(tracksState.tracks)
            is TracksState.Empty -> showNothingWasFoundPlaceholder()
            is TracksState.Error -> showErrorMessagePlaceholder(R.drawable.ic_search_no_internet, R.string.no_internet_connection)
        }
    }

    private fun renderSearchHistoryTracks(searchHistoryTracks: List<Track>) {
        if (searchHistoryTracks.isNotEmpty()) {
            binding.recyclerViewTracks.visibility = View.GONE
            binding.linearLayoutPlaceholderMessage.visibility = View.GONE
            binding.linearLayoutSearchHistory.visibility = View.VISIBLE
            searchHistoryTrackAdapter.tracks.clear()
            searchHistoryTrackAdapter.tracks.addAll(searchHistoryTracks)
            searchHistoryTrackAdapter.notifyDataSetChanged()
        } else {
            binding.linearLayoutSearchHistory.visibility = View.GONE
            searchHistoryTrackAdapter.tracks.clear()
            searchHistoryTrackAdapter.notifyDataSetChanged()
        }
    }

    companion object {
        private const val MAX_SIZE_OF_SEARCH_HISTORY_TRACKS = 10
        private const val CLICK_DEBOUNCE_DELAY = 1000L
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
    }
}