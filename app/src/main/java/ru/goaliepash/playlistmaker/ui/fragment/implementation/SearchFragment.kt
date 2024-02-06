package ru.goaliepash.playlistmaker.ui.fragment.implementation

import android.annotation.SuppressLint
import android.content.Context
import android.os.Bundle
import android.text.Editable
import android.text.TextWatcher
import android.view.LayoutInflater
import android.view.MotionEvent
import android.view.View
import android.view.ViewGroup
import android.view.inputmethod.InputMethodManager
import android.widget.EditText
import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import androidx.lifecycle.lifecycleScope
import androidx.navigation.fragment.findNavController
import androidx.recyclerview.widget.LinearLayoutManager
import org.koin.androidx.viewmodel.ext.android.viewModel
import ru.goaliepash.domain.model.Track
import ru.goaliepash.playlistmaker.R
import ru.goaliepash.playlistmaker.databinding.FragmentSearchBinding
import ru.goaliepash.playlistmaker.presentation.state.SearchHistoryTracksState
import ru.goaliepash.playlistmaker.presentation.state.TracksState
import ru.goaliepash.playlistmaker.presentation.view_model.SearchViewModel
import ru.goaliepash.playlistmaker.ui.adapter.TrackAdapter
import ru.goaliepash.playlistmaker.ui.fragment.BindingFragment
import ru.goaliepash.playlistmaker.utils.Constants.CLICK_DEBOUNCE_DELAY
import ru.goaliepash.playlistmaker.utils.debounce

class SearchFragment : BindingFragment<FragmentSearchBinding>() {

    private val viewModel by viewModel<SearchViewModel>()

    private lateinit var trackAdapter: TrackAdapter
    private lateinit var searchHistoryTrackAdapter: TrackAdapter
    private lateinit var searchTextWatcher: TextWatcher
    private lateinit var onTrackClickDebounce: (Track) -> Unit

    override fun createBinding(
        inflater: LayoutInflater,
        container: ViewGroup?
    ): FragmentSearchBinding {
        return FragmentSearchBinding.inflate(inflater, container, false)
    }

    override fun onViewCreated(view: View, savedInstanceState: Bundle?) {
        super.onViewCreated(view, savedInstanceState)
        onTrackClickDebounce =
            debounce(CLICK_DEBOUNCE_DELAY, viewLifecycleOwner.lifecycleScope, false) {
                onTrackClick(it)
            }
        initUI()
        viewModel.getSearchHistoryTracksState().observe(viewLifecycleOwner) {
            renderSearchHistoryTracksState(it)
        }
        viewModel.getTracksState().observe(viewLifecycleOwner) {
            renderTracksState(it)
        }
    }

    override fun onResume() {
        super.onResume()
        viewModel.isScreenOnPaused = false
    }

    override fun onPause() {
        super.onPause()
        viewModel.isScreenOnPaused = true
    }

    private fun initUI() {
        initEditTextSearch()
        initRecyclerViewTracks()
        initButtonPlaceholderRefresh()
        initSearchHistory()
    }

    private fun initEditTextSearch() {
        binding.editTextSearch.setOnFocusChangeListener { _, b ->
            if (b && binding.editTextSearch.text.isEmpty()) {
                viewModel.getSearchHistory()
            } else {
                binding.linearLayoutSearchHistory.visibility = View.GONE
            }
        }
        searchTextWatcher = object : TextWatcher {
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
                viewModel.search(binding.editTextSearch.text.toString())
            }

            override fun afterTextChanged(p0: Editable?) {}
        }
        searchTextWatcher.let {
            binding.editTextSearch.addTextChangedListener(it)
        }
        binding.editTextSearch.onDrawableEndClick {
            onEditTextSearchClearClick()
        }
        setDrawableEndVisibility(false, binding.editTextSearch)
    }

    private fun initRecyclerViewTracks() {
        trackAdapter = TrackAdapter(onTrackClickDebounce)
        binding.recyclerViewTracks.adapter = trackAdapter
        binding.recyclerViewTracks.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun initButtonPlaceholderRefresh() {
        binding.buttonPlaceholderRefresh.setOnClickListener { onButtonPlaceHolderRefresh() }
    }

    private fun initSearchHistory() {
        initRecyclerViewSearchHistory()
        initButtonClearHistory()
    }

    private fun initRecyclerViewSearchHistory() {
        searchHistoryTrackAdapter = TrackAdapter(onTrackClickDebounce)
        binding.recyclerViewSearchHistory.adapter = searchHistoryTrackAdapter
        binding.recyclerViewSearchHistory.layoutManager = LinearLayoutManager(requireActivity())
    }

    private fun initButtonClearHistory() {
        binding.buttonClearSearchHistory.setOnClickListener { viewModel.clearSearchHistory() }
    }

    private fun setDrawableEndVisibility(isVisible: Boolean, editText: EditText) {
        if (isVisible) {
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(
                R.drawable.ic_search,
                0,
                R.drawable.ic_close,
                0
            )
        } else {
            editText.setCompoundDrawablesRelativeWithIntrinsicBounds(R.drawable.ic_search, 0, 0, 0)
        }
    }

    private fun hideKeyboard(editText: EditText) {
        val inputMethodManager =
            requireActivity().getSystemService(Context.INPUT_METHOD_SERVICE) as? InputMethodManager
        inputMethodManager?.hideSoftInputFromWindow(editText.windowToken, 0)
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun EditText.onDrawableEndClick(action: () -> Unit) {
        setOnTouchListener { view, motionEvent ->
            if (motionEvent.action == MotionEvent.ACTION_UP) {
                view as EditText
                val end =
                    if (view.resources.configuration.layoutDirection == View.LAYOUT_DIRECTION_RTL) view.left else right
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
        binding.linearLayoutProgressBar.visibility = View.GONE
        trackAdapter?.let {
            it.tracks.clear()
            it.tracks.addAll(tracks)
            it.notifyDataSetChanged()
        }
    }

    private fun showNothingWasFoundPlaceholder() {
        binding.recyclerViewTracks.visibility = View.GONE
        binding.linearLayoutPlaceholderMessage.visibility = View.VISIBLE
        binding.linearLayoutSearchHistory.visibility = View.GONE
        binding.imageViewPlaceholderMessage.setBackgroundResource(R.drawable.ic_search_error)
        binding.textViewPlaceholderMessage.text = getString(R.string.nothing_was_found)
        binding.buttonPlaceholderRefresh.visibility = View.GONE
        binding.linearLayoutProgressBar.visibility = View.GONE
    }

    private fun showErrorMessagePlaceholder(
        @DrawableRes backgroundResource: Int,
        @StringRes text: Int
    ) {
        binding.recyclerViewTracks.visibility = View.GONE
        binding.linearLayoutPlaceholderMessage.visibility = View.VISIBLE
        binding.linearLayoutSearchHistory.visibility = View.GONE
        binding.imageViewPlaceholderMessage.setBackgroundResource(backgroundResource)
        binding.textViewPlaceholderMessage.text = getString(text)
        binding.buttonPlaceholderRefresh.visibility = View.VISIBLE
        binding.linearLayoutProgressBar.visibility = View.GONE
    }

    private fun onEditTextSearchClearClick() {
        binding.editTextSearch.setText("")
        setDrawableEndVisibility(false, binding.editTextSearch)
        hideKeyboard(binding.editTextSearch)
        trackAdapter?.let {
            it.tracks.clear()
            it.notifyDataSetChanged()
        }
        binding.linearLayoutPlaceholderMessage.visibility = View.GONE
        viewModel.clearTrackState()
        viewModel.getSearchHistory()
    }

    private fun onTrackClick(track: Track) {
        viewModel.addSearchHistory(track)
        openTrackActivity(track)
    }

    private fun openTrackActivity(track: Track) {
        findNavController().navigate(
            R.id.action_searchFragment_to_audioPlayerFragment,
            AudioPlayerFragment.createArgs(track)
        )
    }

    private fun showLoading() {
        binding.linearLayoutProgressBar.visibility = View.VISIBLE
    }

    private fun hideLoading() {
        binding.linearLayoutProgressBar.visibility = View.GONE
    }

    private fun onButtonPlaceHolderRefresh() {
        binding.linearLayoutPlaceholderMessage.visibility = View.GONE
        viewModel.refreshSearch(binding.editTextSearch.text.toString())
    }

    private fun renderTracksState(state: TracksState) {
        when (state) {
            is TracksState.Loading -> showLoading()
            is TracksState.Content -> showTracks(state.tracks)
            is TracksState.Empty -> showNothingWasFoundPlaceholder()
            is TracksState.Error -> showErrorMessagePlaceholder(
                R.drawable.ic_search_no_internet,
                R.string.no_internet_connection
            )

            is TracksState.Cancel -> {
                viewModel.clearSearchHistoryTrackState()
                hideLoading()
            }
        }
    }

    private fun renderSearchHistoryTracksState(state: SearchHistoryTracksState) {
        when (state) {
            is SearchHistoryTracksState.Addition -> {
                searchHistoryTrackAdapter.tracks.clear()
                searchHistoryTrackAdapter.tracks.addAll(state.tracks)
                searchHistoryTrackAdapter.notifyDataSetChanged()
            }

            is SearchHistoryTracksState.Receipt -> {
                binding.recyclerViewTracks.visibility = View.GONE
                binding.linearLayoutPlaceholderMessage.visibility = View.GONE
                binding.linearLayoutSearchHistory.visibility = View.VISIBLE
                searchHistoryTrackAdapter.tracks.clear()
                searchHistoryTrackAdapter.tracks.addAll(state.tracks)
                searchHistoryTrackAdapter.notifyDataSetChanged()
            }

            is SearchHistoryTracksState.Cleaning -> {
                binding.linearLayoutSearchHistory.visibility = View.GONE
                searchHistoryTrackAdapter.tracks.clear()
                searchHistoryTrackAdapter.notifyDataSetChanged()
            }
        }
    }
}