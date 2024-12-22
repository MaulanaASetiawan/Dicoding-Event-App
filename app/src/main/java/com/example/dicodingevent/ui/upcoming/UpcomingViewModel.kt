package com.example.dicodingevent.ui.upcoming

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.data.remote.response.ListEventsItem
class UpcomingViewModel(private val eventRepository: EventRepository) : ViewModel() {

    private val _upComingModel = MutableLiveData<List<ListEventsItem>>()
    val upComingModel: LiveData<List<ListEventsItem>> get() = _upComingModel

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    init {
        getItem()
    }

    fun getItem(){
        _isLoading.value = true
        eventRepository.getUpcomingEvent().observeForever { result ->
            when (result) {
                is Result.Success -> {
                    _isLoading.value = false
                    _upComingModel.value = result.data
                }
                is Result.Error -> {
                    _isLoading.value = false
                    _errorMessage.value = result.error
                }
                is Result.Loading -> {
                    _isLoading.value = true
                }
            }
        }
    }

    fun resetErrorMessage() {
        _errorMessage.value = null
    }
}
