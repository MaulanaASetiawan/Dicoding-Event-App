package com.example.dicodingevent.ui.search

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.remote.response.ListEventsItem
import com.example.dicodingevent.data.Result

class SearchViewModel(private val eventRepository: EventRepository) : ViewModel() {
    private val _searchEvent = MutableLiveData<List<ListEventsItem>>()
    val searchEvent: LiveData<List<ListEventsItem>> = _searchEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> = _errorMessage

    fun searchEvent(q: String){
        _isLoading.value = true
        eventRepository.searchEvent(q).observeForever { result ->
            when(result){
                is Result.Success -> {
                    _isLoading.value = false
                    _searchEvent.value = result.data
                }
                is Result.Error -> {
                    _isLoading.value = false
                    _errorMessage.value = result.error
                }
                is Result.Loading -> {
                    _isLoading.value = true
                }
                else -> {
                    _isLoading.value = false
                    _errorMessage.value = "Unknown Error"
                }
            }
        }
    }
}