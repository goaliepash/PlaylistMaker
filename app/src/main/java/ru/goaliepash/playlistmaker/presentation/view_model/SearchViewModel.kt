package ru.goaliepash.playlistmaker.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import kotlinx.coroutines.flow.catch
import kotlinx.coroutines.launch
import ru.goaliepash.domain.interactor.ItunesInteractor
import ru.goaliepash.domain.interactor.SearchHistoryInteractor
import ru.goaliepash.domain.model.Track
import ru.goaliepash.playlistmaker.presentation.state.SearchHistoryTracksState
import ru.goaliepash.playlistmaker.presentation.state.TracksState
import ru.goaliepash.playlistmaker.utils.Constants.SEARCH_DEBOUNCE_DELAY
import ru.goaliepash.playlistmaker.utils.debounce

class SearchViewModel(private val itunesInteractor: ItunesInteractor, private val searchHistoryInteractor: SearchHistoryInteractor) : ViewModel() {

    var isScreenOnPaused = false

    private val tracksState = MutableLiveData<TracksState>()
    private val searchHistoryTracksState = MutableLiveData<SearchHistoryTracksState>()
    private val searchDebounce = debounce<String>(SEARCH_DEBOUNCE_DELAY, viewModelScope, true) {
        searchRequest(it)
    }

    private var latestSearchText: String? = null

    fun getTracksState(): LiveData<TracksState> = tracksState

    fun getSearchHistoryTracksState(): LiveData<SearchHistoryTracksState> = searchHistoryTracksState

    fun search(term: String) {
        if (latestSearchText != term) {
            latestSearchText = term
            searchDebounce(term)
        }
    }

    fun refreshSearch(term: String) {
        searchRequest(term)
    }

    fun clearTrackState() {
        tracksState.value = TracksState.Content(emptyList())
    }

    fun clearSearchHistoryTrackState() {
        searchHistoryTracksState.value = SearchHistoryTracksState.Cleaning(emptyList())
    }

    fun addSearchHistory(track: Track) {
        val tracks = mutableListOf<Track>()
        tracks.addAll(searchHistoryInteractor.getSearchHistory())
        if (tracks.contains(track)) {
            tracks.remove(track)
        } else if (tracks.size == MAX_SIZE_OF_SEARCH_HISTORY_TRACKS) {
            tracks.removeAt(MAX_SIZE_OF_SEARCH_HISTORY_TRACKS - 1)
        }
        tracks.add(0, track)
        searchHistoryInteractor.addSearchHistory(tracks)
        renderSearchHistoryTrackState(SearchHistoryTracksState.Addition(tracks))
    }

    fun getSearchHistory() {
        if (searchHistoryInteractor.getSearchHistory().isNotEmpty()) {
            renderSearchHistoryTrackState(SearchHistoryTracksState.Receipt(searchHistoryInteractor.getSearchHistory().toList()))
        }
    }

    fun clearSearchHistory() {
        searchHistoryInteractor.clearSearchHistory()
        renderSearchHistoryTrackState(SearchHistoryTracksState.Cleaning(emptyList()))
    }

    private fun searchRequest(term: String) {
        if (term.isNotEmpty()) {
            renderTracksState(TracksState.Loading)
            viewModelScope.launch {
                itunesInteractor
                    .getSearch(term)
                    .catch { renderTracksState(TracksState.Error) }
                    .collect {
                        if (!isScreenOnPaused) {
                            if (it.isNotEmpty()) {
                                renderTracksState(TracksState.Content(it))
                            } else {
                                renderTracksState(TracksState.Empty)
                            }
                        } else {
                            renderTracksState(TracksState.Cancel)
                        }
                    }
            }
        }
    }

    private fun renderTracksState(state: TracksState) {
        tracksState.postValue(state)
    }

    private fun renderSearchHistoryTrackState(state: SearchHistoryTracksState) {
        searchHistoryTracksState.value = state
    }

    companion object {
        private const val MAX_SIZE_OF_SEARCH_HISTORY_TRACKS = 10
    }
}