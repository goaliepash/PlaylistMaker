package ru.goaliepash.playlistmaker.presentation.view_model

import android.content.Context
import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import androidx.lifecycle.viewmodel.initializer
import androidx.lifecycle.viewmodel.viewModelFactory
import io.reactivex.rxjava3.android.schedulers.AndroidSchedulers
import io.reactivex.rxjava3.core.Observable
import io.reactivex.rxjava3.disposables.CompositeDisposable
import io.reactivex.rxjava3.schedulers.Schedulers
import ru.goaliepash.playlistmaker.domain.model.Track
import ru.goaliepash.playlistmaker.presentation.state.TracksState
import ru.goaliepash.playlistmaker.util.Creator

class SearchViewModel(context: Context) : ViewModel() {

    private val getSearchUseCase by lazy { Creator.provideGetSearchUseCase() }
    private val addSearchHistoryUseCase by lazy { Creator.provideAddSearchHistoryUseCase(context) }
    private val getSearchHistoryUseCase by lazy { Creator.provideGetSearchHistoryUseCase(context) }
    private val clearSearchHistoryUseCase by lazy { Creator.provideClearSearchHistoryUseCase(context) }
    private val tracksState = MutableLiveData<TracksState>()
    private val searchHistoryTracks = MutableLiveData<ArrayList<Track>>()
    private val compositeDisposable = CompositeDisposable()

    override fun onCleared() {
        super.onCleared()
        compositeDisposable.clear()
    }

    fun getTracksState(): LiveData<TracksState> = tracksState

    fun getSearchHistoryTracks(): LiveData<ArrayList<Track>> = searchHistoryTracks

    fun search(term: String) {
        val observable: Observable<List<Track>> = Observable
            .fromCallable { getSearchUseCase(term) }
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

    fun addSearchHistory(tracks: List<Track>) {
        addSearchHistoryUseCase(tracks)
    }

    fun getSearchHistory() {
        val trackList = ArrayList<Track>()
        trackList.addAll(getSearchHistoryUseCase())
        searchHistoryTracks.value = trackList
    }

    fun clearSearchHistory() {
        searchHistoryTracks.value = ArrayList()
        clearSearchHistoryUseCase()
    }

    private fun renderTracksState(state: TracksState) {
        tracksState.postValue(state)
    }

    companion object {
        fun getViewModelFactory(context: Context): ViewModelProvider.Factory = viewModelFactory { initializer { SearchViewModel(context) } }
    }
}