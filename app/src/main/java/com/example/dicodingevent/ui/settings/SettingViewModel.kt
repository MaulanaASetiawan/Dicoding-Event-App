package com.example.dicodingevent.ui.settings

import android.app.Application
import androidx.lifecycle.AndroidViewModel
import androidx.lifecycle.LiveData
import androidx.lifecycle.asLiveData
import androidx.lifecycle.viewModelScope
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import kotlinx.coroutines.launch
import java.util.concurrent.TimeUnit

class SettingViewModel(application: Application, private val settingPreferences: SettingPreferences) : AndroidViewModel(application) {
    fun getThemeSettings(): LiveData<Boolean> {
        return settingPreferences.getThemeSetting().asLiveData()
    }

    fun saveThemeSettings(isDarkModeActive: Boolean) {
        viewModelScope.launch {
            settingPreferences.saveThemeSetting(isDarkModeActive)
        }
    }

    fun getReminderSettings(): LiveData<Boolean> {
        return settingPreferences.getReminderSetting().asLiveData()
    }

    fun saveReminderSettings(isReminderActive: Boolean) {
        viewModelScope.launch {
            settingPreferences.saveReminderSetting(isReminderActive)
        }
    }

    fun enableDailyReminder() {
        val workRequest = PeriodicWorkRequestBuilder<MyWorker>(24, TimeUnit.HOURS)
            .addTag("dailyReminder")
            .build()
        WorkManager.getInstance(getApplication()).enqueue(workRequest)
    }

    fun disableDailyReminder() {
        WorkManager.getInstance(getApplication()).cancelAllWorkByTag("dailyReminder")
    }
}