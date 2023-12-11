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
import com.example.techjunction.constants.CHANNEL_URL_HATENA
import com.example.techjunction.constants.CHANNEL_URL_ZENN
import com.example.techjunction.domain.RssParser
import com.example.techjunction.room.RssDatabase
import com.example.techjunction.room.RssRepositoryImpl
import com.example.techjunction.util.HttpDownloadManager
import com.example.techjunction.util.WorkScheduler
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.File
import java.util.Date
import java.util.concurrent.TimeUnit

class RssDownloadWorker(
    private val context: Context,
    params: WorkerParameters
): CoroutineWorker(context, params) {
    companion object {
        const val TAG = "com.example.techjunction.worker.RssDownloadWorker"

        fun start(context: Context): Boolean {
            if (isWorkScheduled(context)) return false
            WorkManager.getInstance(context).enqueue(createRequest())

            return  true
        }

        private fun isWorkScheduled(context: Context): Boolean {
            return WorkScheduler.isWorkScheduled(context, TAG)
        }

        private fun createRequest(): OneTimeWorkRequest {
            val constraints = Constraints.Builder()
                .setRequiredNetworkType(NetworkType.CONNECTED)
                .setRequiresBatteryNotLow(true)
                .setRequiresStorageNotLow(true)
                .build()

            return OneTimeWorkRequest.Builder(RssDownloadWorker::class.java)
                .setConstraints(constraints)
                .setBackoffCriteria(BackoffPolicy.EXPONENTIAL, 1, TimeUnit.MINUTES)
                .addTag(TAG)
                .build()
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
        val repo = RssRepositoryImpl(db)
        val parser = RssParser(repo)
        setUpChannels(repo)
        repo.getChannels().forEach { channel ->
            val uri = Uri.parse(channel.rssUrl)
            val downloadResult = HttpDownloadManager.writeData(uri, rssFile)
            if (downloadResult) {
                rssFile.inputStream().use { input ->
                    parser.parse(input, channel.rssUrl)
                }
            }
        }
    }

    private suspend fun setUpChannels(repo: RssRepositoryImpl) {
        if (repo.getChannels().isEmpty()) {
            repo.insertOrUpdateChannel(
                CHANNEL_URL_HATENA,
                null,
                null,
                null,
                Date()
            )
            repo.insertOrUpdateChannel(
                CHANNEL_URL_ZENN,
                null,
                null,
                null,
                Date()
            )
        }
    }
}