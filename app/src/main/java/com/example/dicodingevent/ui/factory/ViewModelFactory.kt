package com.example.dicodingevent.ui.factory

import android.app.Application
import android.content.Context
import androidx.datastore.core.DataStore
import androidx.datastore.preferences.core.Preferences
import androidx.lifecycle.ViewModel
import androidx.lifecycle.ViewModelProvider
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.di.Injection
import com.example.dicodingevent.ui.detail.DetailViewModel
import com.example.dicodingevent.ui.favorite.FavoriteViewModel
import com.example.dicodingevent.ui.finished.FinishedViewModel
import com.example.dicodingevent.ui.home.HomeViewModel
import com.example.dicodingevent.ui.search.SearchViewModel
import com.example.dicodingevent.ui.settings.SettingPreferences
import com.example.dicodingevent.ui.settings.SettingViewModel
import com.example.dicodingevent.ui.settings.dataStore
import com.example.dicodingevent.ui.upcoming.UpcomingViewModel

class ViewModelFactory private constructor(
    private val application: Application,
    private val eventRepository: EventRepository,
    private val dataStore: DataStore<Preferences>
) : ViewModelProvider.NewInstanceFactory() {
    @Suppress("UNCHECKED_CAST")
    override fun <T : ViewModel> create(modelClass: Class<T>): T {
        return when {
            modelClass.isAssignableFrom(FavoriteViewModel::class.java) -> {
                FavoriteViewModel(eventRepository) as T
            }
            modelClass.isAssignableFrom(DetailViewModel::class.java) -> {
                DetailViewModel(eventRepository) as T
            }
            modelClass.isAssignableFrom(FinishedViewModel::class.java) -> {
                FinishedViewModel(eventRepository) as T
            }
            modelClass.isAssignableFrom(HomeViewModel::class.java) -> {
                HomeViewModel(eventRepository) as T
            }
            modelClass.isAssignableFrom(UpcomingViewModel::class.java) -> {
                UpcomingViewModel(eventRepository) as T
            }
            modelClass.isAssignableFrom(SearchViewModel::class.java) -> {
                SearchViewModel(eventRepository) as T
            }
            modelClass.isAssignableFrom(SettingViewModel::class.java) -> {
                val settingPreferences = SettingPreferences.getInstance(dataStore)
                SettingViewModel(application, settingPreferences) as T
            }
            else -> throw IllegalArgumentException("Unknown ViewModel class: " + modelClass.name)
        }
    }

    companion object {
        @Volatile
        private var instance: ViewModelFactory? = null
        fun getInstance(context: Context): ViewModelFactory =
            instance ?: synchronized(this) {
                val application = context.applicationContext as Application
                val dataStore = context.dataStore
                val eventRepository = Injection.provideRepository(context)
                instance ?: ViewModelFactory(application, eventRepository, dataStore)
            }.also { instance = it }
    }
}