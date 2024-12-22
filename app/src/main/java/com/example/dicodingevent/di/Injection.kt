package com.example.dicodingevent.di

import android.content.Context
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.local.room.FavoriteDatabase
import com.example.dicodingevent.data.remote.retrofit.ApiConfig

object Injection  {
    fun provideRepository(context: Context): EventRepository {
        val apiService = ApiConfig.getApiService()
        val database = FavoriteDatabase.getInstance(context)
        val dao = database.favoriteDao()
        return EventRepository.getInstance(dao, apiService)
    }
}