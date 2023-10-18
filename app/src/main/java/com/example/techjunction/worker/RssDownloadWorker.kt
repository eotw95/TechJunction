package com.example.techjunction.worker

import android.content.Context
import androidx.work.BackoffPolicy
import androidx.work.Constraints
import androidx.work.CoroutineWorker
import androidx.work.NetworkType
import androidx.work.OneTimeWorkRequest
import androidx.work.WorkManager
import androidx.work.WorkerParameters
import java.util.concurrent.TimeUnit

class RssDownloadWorker(
    context: Context,
    params: WorkerParameters
): CoroutineWorker(context, params) {
    companion object {
        const val TAG = "RssDownloadWorker"

        private fun isWorkScheduled(context: Context): Boolean {
            // Todo
        }

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
    }
    override suspend fun doWork(): Result {
        TODO("Not yet implemented")
    }
}