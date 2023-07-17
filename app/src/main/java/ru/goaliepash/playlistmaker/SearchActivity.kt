package ru.goaliepash.playlistmaker

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.MotionEvent
import android.view.View
import android.view.inputmethod.EditorInfo
import android.view.inputmethod.InputMethodManager
import android.widget.Button
import android.widget.EditText
import android.widget.ImageView
import android.widget.LinearLayout
import android.widget.TextView
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.appcompat.app.AppCompatActivity
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import retrofit2.Call
import retrofit2.Callback
import retrofit2.Response
import ru.goaliepash.playlistmaker.model.SearchResponse
import ru.goaliepash.playlistmaker.model.Track

class SearchActivity : AppCompatActivity() {

    private val itunesRepository = ItunesRepository(ItunesService.getItunesApi())
    private val tracks = ArrayList<Track>()
    private val trackAdapter = TrackAdapter(tracks)

    private lateinit var imageViewBack: ImageView
    private lateinit var editTextSearch: EditText
    private lateinit var recyclerViewTracks: RecyclerView
    private lateinit var linearLayoutPlaceholderMessage: LinearLayout
    private lateinit var imageViewPlaceholderMessage: ImageView
    private lateinit var textViewPlaceholderMessage: TextView
    private lateinit var buttonPlaceholderRefresh: Button

    private var textSearch: String = ""

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_search)
        initUI()
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
    }

    private fun initImageViewBack() {
        imageViewBack = findViewById(R.id.image_view_back)
        imageViewBack.setOnClickListener { finish() }
    }

    private fun initEditTextSearch() {
        editTextSearch = findViewById(R.id.edit_text_search)
        val searchTextWatcher = object : TextWatcher {
            override fun beforeTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {}

            override fun onTextChanged(p0: CharSequence?, p1: Int, p2: Int, p3: Int) {
                if (p0.isNullOrEmpty()) {
                    setDrawableEndVisibility(false, editTextSearch)
                } else {
                    setDrawableEndVisibility(true, editTextSearch)
                }
                textSearch = p0.toString()
            }

            override fun afterTextChanged(p0: Editable?) {}
        }
        editTextSearch.addTextChangedListener(searchTextWatcher)
        editTextSearch.onDrawableEndClick { clickOnEditTextSearchClear() }
        setDrawableEndVisibility(false, editTextSearch)
        editTextSearch.setOnEditorActionListener { _, i, _ ->
            if (i == EditorInfo.IME_ACTION_DONE) {
                search(textSearch)
                true
            }
            false
        }
    }

    private fun setDrawableEndVisibility(isVisible: Boolean, editText: EditText) {
        if (isVisible) {
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_search, 0, R.drawable.ic_close, 0)
        } else {
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0)
        }
    }

    private fun hideKeyboard(editText: EditText) {
        val inputMethodManager =
            getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
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

    private fun initRecyclerViewTracks() {
        recyclerViewTracks = findViewById(R.id.recycler_view_tracks)
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
        buttonPlaceholderRefresh.setOnClickListener { search(textSearch) }
    }

    private fun search(term: String) {
        itunesRepository
            .getSearch(term)
            .enqueue(object : Callback<SearchResponse> {
                override fun onResponse(call: Call<SearchResponse>, response: Response<SearchResponse>) {
                    when (response.code()) {
                        200 -> {
                            if (response.body()?.results?.isNotEmpty() == true) {
                                showTracks(response.body()?.results!!)
                            } else {
                                showNothingWasFoundPlaceholder()
                            }
                        }

                        else -> showErrorMessagePlaceholder(R.drawable.ic_search_error, R.string.something_went_wrong)
                    }
                }

                override fun onFailure(call: Call<SearchResponse>, t: Throwable) {
                    showErrorMessagePlaceholder(R.drawable.ic_search_no_internet, R.string.no_internet_connection)
                }
            })
    }

    private fun showTracks(results: List<Track>) {
        recyclerViewTracks.visibility = View.VISIBLE
        linearLayoutPlaceholderMessage.visibility = View.GONE
        tracks.clear()
        tracks.addAll(results)
        trackAdapter.notifyDataSetChanged()
    }

    private fun showNothingWasFoundPlaceholder() {
        recyclerViewTracks.visibility = View.GONE
        linearLayoutPlaceholderMessage.visibility = View.VISIBLE
        imageViewPlaceholderMessage.setBackgroundResource(R.drawable.ic_search_error)
        textViewPlaceholderMessage.text = getString(R.string.nothing_was_found)
        buttonPlaceholderRefresh.visibility = View.GONE
    }

    private fun showErrorMessagePlaceholder(@DrawableRes backgroundResource: Int, @StringRes text: Int) {
        recyclerViewTracks.visibility = View.GONE
        linearLayoutPlaceholderMessage.visibility = View.VISIBLE
        imageViewPlaceholderMessage.setBackgroundResource(backgroundResource)
        textViewPlaceholderMessage.text = getString(text)
        buttonPlaceholderRefresh.visibility = View.VISIBLE
    }

    private fun clickOnEditTextSearchClear() {
        textSearch = ""
        editTextSearch.setText(textSearch)
        setDrawableEndVisibility(false, editTextSearch)
        hideKeyboard(editTextSearch)
        tracks.clear()
        trackAdapter.notifyDataSetChanged()
        linearLayoutPlaceholderMessage.visibility = View.GONE
    }

    companion object {
        private const val SEARCH_STRING = "SEARCH_STRING"
    }
}