package ru.goaliepash.playlistmaker.presentation.view_model

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.goaliepash.domain.interactor.ItunesInteractor
import ru.goaliepash.domain.interactor.SearchHistoryInteractor
import ru.goaliepash.domain.model.Track
import ru.goaliepash.playlistmaker.presentation.state.SearchHistoryTracksState
import ru.goaliepash.playlistmaker.presentation.state.TracksState

class SearchViewModel(private val itunesInteractor: ItunesInteractor, private val searchHistoryInteractor: SearchHistoryInteractor) : ViewModel() {

    private val tracksState = MutableLiveData<TracksState>()
    private val searchHistoryTracksState = MutableLiveData<SearchHistoryTracksState>()
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getTracksState(): LiveData<TracksState> = tracksState

    fun getSearchHistoryTracksState(): LiveData<SearchHistoryTracksState> = searchHistoryTracksState

    fun search(term: String) {
        val observable: Observable<List<Track>> = Observable
            .fromCallable { itunesInteractor.getSearch(term) }
            .subscribeOn(Schedulers.io())
            .observeOn(AndroidSchedulers.mainThread())
            .doOnSubscribe { renderTracksState(TracksState.Loading) }
        val disposable = observable
            .subscribe(
                { result ->
                    if (result.isNotEmpty()) {
                        renderTracksState(TracksState.Content(result))
                    } else {
                        renderTracksState(TracksState.Empty)
                    }
                },
                { _ -> renderTracksState(TracksState.Error) }
            )
        compositeDisposable.add(disposable)
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