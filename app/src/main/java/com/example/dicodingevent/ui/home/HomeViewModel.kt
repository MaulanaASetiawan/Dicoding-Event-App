package com.example.dicodingevent.ui.home

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.remote.response.ListEventsItem
import com.example.dicodingevent.data.Result

class HomeViewModel(private val eventRepository: EventRepository) : ViewModel() {
    private val _upcomingEvent = MutableLiveData<List<ListEventsItem>>()
    val upcomingEvent: LiveData<List<ListEventsItem>> = _upcomingEvent

    private val _finishedEvent = MutableLiveData<List<ListEventsItem>>()
    val finishedEvent: LiveData<List<ListEventsItem>> = _finishedEvent

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> = _isLoading

    private val _message = MutableLiveData<String>()
    val message: LiveData<String> = _message

    fun getFinishedEvent(){
        _isLoading.value = true
        eventRepository.getFinished5Event().observeForever { result ->
            when(result) {
                is Result.Success -> {
                    _isLoading.value = false
                    _finishedEvent.value = result.data
                }
                is Result.Error -> {
                    _isLoading.value = false
                    _message.value = result.error
                }
                is Result.Loading -> {
                    _isLoading.value = true
                }
                else -> {
                    _isLoading.value = false
                    _message.value = "Unknown Error"
                }
            }
        }
    }

    fun getUpcomingEvent(){
        _isLoading.value = true
        eventRepository.getUpcoming5Event().observeForever { result ->
            when(result) {
                is Result.Success -> {
                    _isLoading.value = false
                    _upcomingEvent.value = result.data
                }
                is Result.Error -> {
                    _isLoading.value = false
                    _message.value = result.error
                }
                is Result.Loading -> {
                    _isLoading.value = true
                }
                else -> {
                    _isLoading.value = false
                    _message.value = "Unknown Error"
                }
            }
        }
    }
}