package com.example.dicodingevent.ui.favorite

import androidx.lifecycle.LiveData
import androidx.lifecycle.ViewModel
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.local.entity.FavoriteEntity

class FavoriteViewModel(private val repository: EventRepository) : ViewModel() {
    fun getAllFavorite(): LiveData<List<FavoriteEntity>> = repository.getAllFavorite()
}