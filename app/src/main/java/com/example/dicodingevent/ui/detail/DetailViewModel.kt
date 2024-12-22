package com.example.dicodingevent.ui.detail

import androidx.lifecycle.LiveData
import androidx.lifecycle.MutableLiveData
import androidx.lifecycle.ViewModel
import androidx.lifecycle.viewModelScope
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.Result
import com.example.dicodingevent.data.local.entity.FavoriteEntity
import com.example.dicodingevent.data.remote.response.Event
import kotlinx.coroutines.launch

class DetailViewModel(private val eventRepository: EventRepository) : ViewModel() {

    private val _events = MutableLiveData<Event>()
    val events: LiveData<Event> get() = _events

    private val _isLoading = MutableLiveData<Boolean>()
    val isLoading: LiveData<Boolean> get() = _isLoading

    private val _errorMessage = MutableLiveData<String>()
    val errorMessage: LiveData<String> get() = _errorMessage

    private val _isFavorite = MutableLiveData<Boolean>()
    val isFavorite: LiveData<Boolean> get() = _isFavorite

    fun getDetail(id: Int){
        _isLoading.value = true
        eventRepository.getDetailEvent(id).observeForever {result ->
            when (result) {
                is Result.Success -> {
                    _isLoading.value = false
                    _events.value = result.data
                }
                is Result.Error -> {
                    _isLoading.value = false
                    _errorMessage.value = result.error
                }
                is Result.Loading -> {
                    _isLoading.value = true
                }
                else -> {
                    _errorMessage.value = "Unknown error:  ${result?.javaClass?.simpleName}"
                }
            }
        }

    }

    fun checkFavorite(id: Int){
        viewModelScope.launch {
            _isFavorite.value = eventRepository.isFavoriteEvent(id)
        }
    }

    fun addEventToFavorite(favorite: FavoriteEntity){
        viewModelScope.launch {
            eventRepository.addToFavorite(favorite)
            _isFavorite.value = true
        }
    }

    fun removeEventFromFavorite(favorite: FavoriteEntity){
        viewModelScope.launch {
            eventRepository.removeFromFavorite(favorite)
            _isFavorite.value = false
        }
    }
}