package com.example.dicodingevent.data

import androidx.lifecycle.LiveData
import androidx.lifecycle.liveData
import com.example.dicodingevent.data.local.entity.FavoriteEntity
import com.example.dicodingevent.data.local.room.FavoriteDao
import com.example.dicodingevent.data.remote.response.Event
import com.example.dicodingevent.data.remote.response.ListEventsItem
import com.example.dicodingevent.data.remote.response.ResponseList
import com.example.dicodingevent.data.remote.retrofit.ApiService

class EventRepository private constructor(
    private val favoriteDao: FavoriteDao,
    private val apiService: ApiService
) {
    fun getAllFavorite(): LiveData<List<FavoriteEntity>> = favoriteDao.getAllFavorite()

    suspend fun addToFavorite(event: FavoriteEntity) = favoriteDao.insert(event)

    suspend fun removeFromFavorite(event: FavoriteEntity) = favoriteDao.delete(event)

    fun getFinishedEvent(): LiveData<Result<List<ListEventsItem>>> = liveData {
        emit(Result.Loading)
        try {
            val responseList = apiService.getFinished(0)
            emit(Result.Success(responseList.listEvents))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }

    fun getUpcomingEvent(): LiveData<Result<List<ListEventsItem>>> = liveData {
        emit(Result.Loading)
        try {
            val responseList = apiService.getUpcoming(1)
            emit(Result.Success(responseList.listEvents))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }

    fun getFinished5Event(): LiveData<Result<List<ListEventsItem>>> = liveData {
        emit(Result.Loading)
        try {
            val responseList = apiService.getFinished5(0, 5)
            emit(Result.Success(responseList.listEvents))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }

    fun getUpcoming5Event(): LiveData<Result<List<ListEventsItem>>> = liveData {
        emit(Result.Loading)
        try {
            val responseList = apiService.getUpcoming5(1, 5)
            emit(Result.Success(responseList.listEvents))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }

    fun searchEvent(query: String): LiveData<Result<List<ListEventsItem>>> = liveData {
        emit(Result.Loading)
        try {
            val responseList = apiService.searchEvents(query)
            emit(Result.Success(responseList.listEvents))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }

    fun getDetailEvent(id: Int): LiveData<Result<Event>> = liveData {
        emit(Result.Loading)
        try {
            val responseDetail = apiService.getDetailEvent(id)
            emit(Result.Success(responseDetail.event))
        } catch (e: Exception) {
            emit(Result.Error(e.message ?: "Unknown error"))
        }
    }

    suspend fun isFavoriteEvent(id: Int): Boolean = favoriteDao.isFavorite(id)

    suspend fun reminderEvent(): ResponseList{
        val fetchData = apiService.getUpcoming5(-1, 1)
        return fetchData
    }

    companion object {
        @Volatile
        private var instance: EventRepository? = null

        fun getInstance(favoriteDao: FavoriteDao, apiService: ApiService): EventRepository =
            instance ?: synchronized(this) {
                instance ?: EventRepository(favoriteDao, apiService).also { instance = it }
            }
    }
}