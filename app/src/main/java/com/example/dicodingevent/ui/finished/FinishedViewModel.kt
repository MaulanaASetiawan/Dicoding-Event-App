package com.example.dicodingevent.ui.finished

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.remote.response.ListEventsItem
import com.example.dicodingevent.data.Result

class FinishedViewModel(private val eventRepository: EventRepository) : ViewModel() {

    private val _finishedModel = MutableLiveData<List<ListEventsItem>>()
    val finishedModel: LiveData<List<ListEventsItem>> get() = _finishedModel

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String?>()
    val errorMessage: LiveData<String?> get() = _errorMessage

    init {
        getFinished()
    }

    fun getFinished() {
        _isLoading.value = true
        eventRepository.getFinishedEvent().observeForever { result ->
            when (result) {
                is Result.Success -> {
                    _isLoading.value = false
                    _finishedModel.value = result.data
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