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
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.ProgressBar
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.goaliepash.playlistmaker.R
import ru.goaliepash.playlistmaker.data.network.itunes.ItunesClientImpl
import ru.goaliepash.playlistmaker.data.repository.ItunesRepositoryImpl
import ru.goaliepash.playlistmaker.data.repository.SearchHistoryRepositoryImpl
import ru.goaliepash.playlistmaker.data.shared_preferences.search_history.SearchHistoryClientImpl
import ru.goaliepash.playlistmaker.domain.model.Track
import ru.goaliepash.playlistmaker.domain.use_case.itunes.GetSearchUseCase
import ru.goaliepash.playlistmaker.domain.use_case.search_history.AddSearchHistoryUseCase
import ru.goaliepash.playlistmaker.domain.use_case.search_history.ClearSearchHistoryUseCase
import ru.goaliepash.playlistmaker.domain.use_case.search_history.GetSearchHistoryUseCase
import ru.goaliepash.playlistmaker.ui.adapter.TrackAdapter
import ru.goaliepash.playlistmaker.ui.listener.OnTrackClickListener
import java.util.LinkedList

class SearchActivity : AppCompatActivity() {

    private val tracks = ArrayList<Track>()
    private val searchHistoryTracks = LinkedList<Track>()
    private val mainThreadHandler = Handler(Looper.getMainLooper())
    private val searchRunnable = Runnable {
        if (textSearch.isNotEmpty()) {
            search(textSearch)
        }
    }
    private val itunesClient by lazy { ItunesClientImpl() }
    private val searchHistoryClient by lazy { SearchHistoryClientImpl(applicationContext) }
    private val itunesRepository by lazy { ItunesRepositoryImpl(itunesClient) }
    private val searchHistoryRepository by lazy { SearchHistoryRepositoryImpl(searchHistoryClient) }
    private val getSearchUseCase by lazy { GetSearchUseCase(itunesRepository) }
    private val addSearchHistoryUseCase by lazy { AddSearchHistoryUseCase(searchHistoryRepository) }
    private val getSearchHistoryUseCase by lazy { GetSearchHistoryUseCase(searchHistoryRepository) }
    private val clearSearchHistoryUseCase by lazy { ClearSearchHistoryUseCase(searchHistoryRepository) }
    private val compositeDisposable = CompositeDisposable()

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var searchHistoryTrackAdapter: TrackAdapter
    private lateinit var imageViewBack: ImageView
    private lateinit var editTextSearch: EditText
    private lateinit var recyclerViewTracks: RecyclerView
    private lateinit var linearLayoutPlaceholderMessage: LinearLayout
    private lateinit var imageViewPlaceholderMessage: ImageView
    private lateinit var textViewPlaceholderMessage: TextView
    private lateinit var buttonPlaceholderRefresh: Button
    private lateinit var linearLayoutSearchHistory: LinearLayout
    private lateinit var textViewSearchHistory: TextView
    private lateinit var recyclerViewSearchHistory: RecyclerView
    private lateinit var buttonClearSearchHistory: Button
    private lateinit var progressBar: ProgressBar

    private var textSearch: String = ""
    private var isClickAllowed = true

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initUI()
    }

    override fun onDestroy() {
        super.onDestroy()
        compositeDisposable.clear()
    }

    override fun onRestoreInstanceState(savedInstanceState: Bundle) {
        super.onRestoreInstanceState(savedInstanceState)
        restoreEditTextSearch(savedInstanceState)
    }

    override fun onSaveInstanceState(outState: Bundle) {
        outState.putString(SEARCH_STRING, textSearch)
        super.onSaveInstanceState(outState)
    }

    private fun initUI() {
        initImageViewBack()
        initEditTextSearch()
        initRecyclerViewTracks()
        initLinearLayoutPlaceholderMessage()
        initSearchHistory()
        initProgressBar()
    }

    private fun initImageViewBack() {
        imageViewBack = findViewById(R.id.image_view_back)
        imageViewBack.setOnClickListener { finish() }
    }

    private fun initEditTextSearch() {
        editTextSearch = findViewById(R.id.edit_text_search)
        editTextSearch.setOnFocusChangeListener { _, b ->
            if (b && editTextSearch.text.isEmpty()) {
                showSearchHistory()
            } else {
                linearLayoutSearchHistory.visibility = View.GONE
            }
        }
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (editTextSearch.hasFocus() && p0?.isEmpty() == true) {
                    showSearchHistory()
                } else {
                    linearLayoutSearchHistory.visibility = View.GONE
                }
                if (p0.isNullOrEmpty()) {
                    setDrawableEndVisibility(false, editTextSearch)
                } else {
                    setDrawableEndVisibility(true, editTextSearch)
                }
                textSearch = p0.toString()
                searchDebounce()
            }

            override fun afterTextChanged(p0: Editable?) {}
        }
        editTextSearch.addTextChangedListener(searchTextWatcher)
        editTextSearch.onDrawableEndClick { onEditTextSearchClearClick() }
        setDrawableEndVisibility(false, editTextSearch)
    }

    private fun initRecyclerViewTracks() {
        recyclerViewTracks = findViewById(R.id.recycler_view_tracks)
        val onTrackClickListener = OnTrackClickListener { track -> onTrackClick(track) }
        trackAdapter = TrackAdapter(tracks, onTrackClickListener)
        recyclerViewTracks.adapter = trackAdapter
        recyclerViewTracks.layoutManager = LinearLayoutManager(this)
    }

    private fun initLinearLayoutPlaceholderMessage() {
        linearLayoutPlaceholderMessage = findViewById(R.id.linear_layout_placeholder_message)
        imageViewPlaceholderMessage = findViewById(R.id.image_view_placeholder_message)
        textViewPlaceholderMessage = findViewById(R.id.text_view_placeholder_message)
        initButtonPlaceholderRefresh()
    }

    private fun initButtonPlaceholderRefresh() {
        buttonPlaceholderRefresh = findViewById(R.id.button_placeholder_refresh)
        buttonPlaceholderRefresh.setOnClickListener { onButtonPlaceHolderRefresh() }
    }

    private fun initSearchHistory() {
        linearLayoutSearchHistory = findViewById(R.id.linear_layout_search_history)
        textViewSearchHistory = findViewById(R.id.text_view_search_history)
        initRecyclerViewSearchHistory()
        initButtonClearHistory()
    }

    private fun initRecyclerViewSearchHistory() {
        recyclerViewSearchHistory = findViewById(R.id.recycler_view_search_history)
        searchHistoryTrackAdapter = TrackAdapter(searchHistoryTracks) { track -> onTrackClick(track) }
        recyclerViewSearchHistory.adapter = searchHistoryTrackAdapter
        recyclerViewSearchHistory.layoutManager = LinearLayoutManager(this)
    }

    private fun initButtonClearHistory() {
        buttonClearSearchHistory = findViewById(R.id.button_clear_search_history)
        buttonClearSearchHistory.setOnClickListener { onButtonClearSearchHistoryClick() }
    }

    private fun initProgressBar() {
        progressBar = findViewById(R.id.progress_bar)
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

    private fun restoreEditTextSearch(savedInstanceState: Bundle) {
        textSearch = savedInstanceState.getString(SEARCH_STRING, "")
        editTextSearch.setText(textSearch)
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

    private fun search(term: String) {
        val observable: Observable<List<Track>> = Observable
            .fromCallable { getSearchUseCase.execute(term) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { showLoader() }
        val disposable = observable
            .subscribe(
                { result ->
                    if (result.isNotEmpty()) {
                        showTracks(result)
                    } else {
                        showNothingWasFoundPlaceholder()
                    }
                },
                { _ ->
                    showErrorMessagePlaceholder(R.drawable.ic_search_no_internet, R.string.no_internet_connection)
                    hideLoader()
                },
                { hideLoader() }
            )
        compositeDisposable.add(disposable)
    }

    private fun showTracks(results: List<Track>) {
        recyclerViewTracks.visibility = View.VISIBLE
        linearLayoutPlaceholderMessage.visibility = View.GONE
        linearLayoutSearchHistory.visibility = View.GONE
        tracks.clear()
        tracks.addAll(results)
        trackAdapter.notifyDataSetChanged()
    }

    private fun showNothingWasFoundPlaceholder() {
        recyclerViewTracks.visibility = View.GONE
        linearLayoutPlaceholderMessage.visibility = View.VISIBLE
        linearLayoutSearchHistory.visibility = View.GONE
        imageViewPlaceholderMessage.setBackgroundResource(R.drawable.ic_search_error)
        textViewPlaceholderMessage.text = getString(R.string.nothing_was_found)
        buttonPlaceholderRefresh.visibility = View.GONE
    }

    private fun showErrorMessagePlaceholder(@DrawableRes backgroundResource: Int, @StringRes text: Int) {
        recyclerViewTracks.visibility = View.GONE
        linearLayoutPlaceholderMessage.visibility = View.VISIBLE
        linearLayoutSearchHistory.visibility = View.GONE
        imageViewPlaceholderMessage.setBackgroundResource(backgroundResource)
        textViewPlaceholderMessage.text = getString(text)
        buttonPlaceholderRefresh.visibility = View.VISIBLE
    }

    private fun showSearchHistory() {
        val tracks = getSearchHistoryUseCase.execute()
        if (tracks.isNotEmpty()) {
            recyclerViewTracks.visibility = View.GONE
            linearLayoutPlaceholderMessage.visibility = View.GONE
            linearLayoutSearchHistory.visibility = View.VISIBLE
            searchHistoryTracks.clear()
            searchHistoryTracks.addAll(tracks)
            searchHistoryTrackAdapter.notifyDataSetChanged()
        }
    }

    private fun onEditTextSearchClearClick() {
        textSearch = ""
        editTextSearch.setText(textSearch)
        setDrawableEndVisibility(false, editTextSearch)
        hideKeyboard(editTextSearch)
        tracks.clear()
        trackAdapter.notifyDataSetChanged()
        linearLayoutPlaceholderMessage.visibility = View.GONE
        showSearchHistory()
    }

    private fun onTrackClick(track: Track) {
        if (clickDebounce()) {
            searchHistoryTracks.clear()
            searchHistoryTracks.addAll(getSearchHistoryUseCase.execute())
            if (searchHistoryTracks.contains(track)) {
                searchHistoryTracks.remove(track)
            } else if (searchHistoryTracks.size == MAX_SIZE_OF_SEARCH_HISTORY_TRACKS) {
                searchHistoryTracks.removeAt(MAX_SIZE_OF_SEARCH_HISTORY_TRACKS - 1)
            }
            searchHistoryTracks.add(0, track)
            addSearchHistoryUseCase.execute(searchHistoryTracks)
            openTrackActivity(track)
            searchHistoryTrackAdapter.notifyDataSetChanged()
        }
    }

    private fun onButtonClearSearchHistoryClick() {
        clearSearchHistoryUseCase.execute()
        linearLayoutSearchHistory.visibility = View.GONE
        searchHistoryTracks.clear()
        searchHistoryTrackAdapter.notifyDataSetChanged()
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

    private fun showLoader() {
        progressBar.visibility = View.VISIBLE
    }

    private fun hideLoader() {
        progressBar.visibility = View.GONE
    }

    private fun onButtonPlaceHolderRefresh() {
        linearLayoutPlaceholderMessage.visibility = View.GONE
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

    companion object {
        private const val SEARCH_STRING = "SEARCH_STRING"
        private const val MAX_SIZE_OF_SEARCH_HISTORY_TRACKS = 10
        private const val SEARCH_DEBOUNCE_DELAY = 2000L
        private const val CLICK_DEBOUNCE_DELAY = 1000L
    }
}