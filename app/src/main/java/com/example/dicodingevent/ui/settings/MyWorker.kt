package com.example.dicodingevent.ui.settings

import android.annotation.SuppressLint
import android.app.NotificationChannel
import android.app.NotificationManager
import android.app.PendingIntent
import android.content.Context
import android.content.Intent
import android.os.Build
import android.util.Log
import androidx.core.app.NotificationCompat
import androidx.core.text.HtmlCompat
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.example.dicodingevent.MainActivity
import com.example.dicodingevent.R
import com.example.dicodingevent.data.EventRepository
import com.example.dicodingevent.data.local.room.FavoriteDatabase
import com.example.dicodingevent.data.remote.retrofit.ApiConfig

class MyWorker(context: Context, workerParams: WorkerParameters) : CoroutineWorker(context, workerParams) {
    override suspend fun doWork(): Result {
        fetchData(applicationContext)
        return Result.success()
    }

    private suspend fun fetchData(context: Context) {
        val favoriteDao = FavoriteDatabase.getInstance(context).favoriteDao()
        val apiService = ApiConfig.getApiService()
        val repository = EventRepository.getInstance(favoriteDao, apiService)
        val events = repository.reminderEvent().listEvents

        if(events.isNotEmpty()){
            val reminder = events[0]
            val desc = HtmlCompat.fromHtml(reminder.description, HtmlCompat.FROM_HTML_MODE_LEGACY).toString()
            showNotification(context, reminder.name,desc)
        } else {
            Log.e("MyWorker", "No data")
        }
    }

    @SuppressLint("ObsoleteSdkInt")
    private fun showNotification(context: Context, title: String, message: String) {
        val notificationManager = context.getSystemService(Context.NOTIFICATION_SERVICE) as NotificationManager
        val channelId = "daily_reminder_channel"
        val channelName = "Daily Reminder"

        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
            val channel = NotificationChannel(channelId, channelName, NotificationManager.IMPORTANCE_DEFAULT)
            notificationManager.createNotificationChannel(channel)
        }

        val intent = Intent(context, MainActivity::class.java)
        val pendingIntent = PendingIntent.getActivity(
            context, 0, intent, PendingIntent.FLAG_UPDATE_CURRENT or PendingIntent.FLAG_IMMUTABLE)

        val notification = NotificationCompat.Builder(context, channelId)
            .setContentTitle(title)
            .setContentText(message)
            .setSmallIcon(R.drawable.baseline_notifications_24)
            .setContentIntent(pendingIntent)
            .setAutoCancel(true)
            .build()

        notificationManager.notify(1, notification)
    }
}