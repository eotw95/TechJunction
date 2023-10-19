package com.example.techjunction.worker

import android.content.Context
import android.net.Uri
import android.os.Environment
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import com.example.techjunction.room.RssDatabase
import com.example.techjunction.room.RssRepositoryImpl
import com.example.techjunction.util.HttpDownloadManager
import com.example.techjunction.util.WorkScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.concurrent.TimeUnit

class RssDownloadWorker(
    private val context: Context,
    params: WorkerParameters
): CoroutineWorker(context, params) {
    companion object {
        const val TAG = "com.example.techjunction.worker.RssDownloadWorker"

        fun start(context: Context): Boolean {
            if (isWorkScheduled(context)) return false

            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .setRequiresStorageNotLow(true)
                .build()

            val request = OneTimeWorkRequest.Builder(RssDownloadWorker::class.java)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.MINUTES)
                .addTag(TAG)
                .build()

            WorkManager.getInstance(context).enqueue(request)

            return  true
        }

        private fun isWorkScheduled(context: Context): Boolean {
            return WorkScheduler.isWorkScheduled(context, TAG)
        }
    }
    override suspend fun doWork(): Result {
        return withContext(Dispatchers.IO) {
            download()
            return@withContext Result.success()
        }
    }

    private suspend fun download() {
        val downloadDir = applicationContext.getExternalFilesDir(Environment.DIRECTORY_DOWNLOADS)
        val rssFile = File(downloadDir, "rssFile")

        val db = RssDatabase.getInstance(context)
        val repo = RssRepositoryImpl()
        repo.fetchChannels().forEach { channel ->
            val uri = Uri.parse(channel.rssUrl)
            val download = HttpDownloadManager.writeData(uri, rssFile)
            // Todo: Rss feedを書き込めたらXMLを解析する
            rssFile.inputStream().use {
                // Todo: XML解析　→　別メソッドに切り出し
            }
        }
    }
}