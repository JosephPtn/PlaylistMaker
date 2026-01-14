package com.saturnnetwork.playlistmaker.search.ui

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.saturnnetwork.playlistmaker.R
import com.saturnnetwork.playlistmaker.search.domain.TracksInteractor
import com.saturnnetwork.playlistmaker.search.domain.models.Track
import kotlinx.coroutines.Job
import kotlinx.coroutines.delay
import kotlinx.coroutines.launch

class SearchViewModel(private val interactor: TracksInteractor): ViewModel() {

    companion object {
        private const val SEARCH_DELAY_MILLIS = 2000L
    }

    private var searchJob: Job? = null
    private var lastSearchText: String = ""

    private val searchText = MutableLiveData<String>("")

    fun searchDebounce(changedText: String) {
        if (lastSearchText == changedText || changedText.isEmpty()) {
            // searchJob?.cancel() -> кейс когда ползователь стирает текст клавишей backspace
            // и нужно не выполнять поиск по последней оставшейся букве в строке
            searchJob?.cancel()
            return
        }

        lastSearchText = changedText

        searchJob?.cancel()
        searchJob = viewModelScope.launch {
            delay(SEARCH_DELAY_MILLIS)
            searchTracks(changedText)
        }
    }

    fun setSearchText(text: String) {
        searchDebounce(text)
        searchText.value = text
    }

    fun getSearchText(): String = searchText.value ?: ""

    val searchStateLiveData = MutableLiveData<SearchState>()
    fun observeSearchStateLiveData(): LiveData<SearchState> = searchStateLiveData

    private val tracksLiveData = MutableLiveData<ArrayList<Track>>()
    fun observeTracksLiveData(): LiveData<ArrayList<Track>> = tracksLiveData

    private var tracksFromHistory = ArrayList<Track>()
    //fun observeTracksFromHistoryLiveData(): LiveData<ArrayList<Track>> = tracksFromHistoryLiveData

    fun getInteractor(): TracksInteractor {
        return interactor
    }

    fun loadFromHistory() {
        tracksFromHistory = interactor.loadFromHistory()
        if (tracksFromHistory.isNotEmpty()) {
            searchStateLiveData.postValue(SearchState(
                tracks = tracksFromHistory,
                isLoading = false,
                errorMessageRes = null,
                composition = "history")
            )
        }
    }

    fun searchTracks(searchInput: String) {
        if (searchInput.isNotEmpty()) {
            searchStateLiveData.postValue(SearchState(
                tracks = arrayListOf<Track>(),
                isLoading = true,
                errorMessageRes = null,
                composition = "search_result")
            )
            interactor.searchTracks(expression = searchInput,
                consumer = object : TracksInteractor.TracksConsumer {
                    override fun consume(foundTracks: ArrayList<Track>) {
                        if (foundTracks.isNotEmpty()) {
                            searchStateLiveData.postValue(SearchState(
                                tracks = foundTracks,
                                isLoading = false,
                                errorMessageRes = null,
                                composition = "search_result")
                            )

                        } else {
                            searchStateLiveData.postValue(SearchState(
                                tracks = arrayListOf<Track>(),
                                isLoading = false,
                                errorMessageRes = R.string.nothing_was_found,
                                composition = "error")
                            )
                        }
                    }

                    override fun onError() {
                        searchStateLiveData.postValue(SearchState(
                            tracks = arrayListOf<Track>(),
                            isLoading = false,
                            errorMessageRes = R.string.connection_issues,
                            composition = "error")
                        )
                    }

                })
        } else {
            searchStateLiveData.postValue(SearchState(
                tracks = ArrayList<Track>(),
                isLoading = false,
                errorMessageRes = null,
                composition = "search_result")
            )
        }

    }

    fun clearHistory() {
        interactor.clearHistory()
        tracksFromHistory.clear()
        searchStateLiveData.postValue(SearchState(
            tracks = tracksFromHistory,
            isLoading = false,
            errorMessageRes = null,
            composition = "search_result")
        )
    }


}