package com.example.techjunction.util

import android.content.Context
import androidx.work.WorkInfo
import androidx.work.WorkManager

class WorkScheduler {
    companion object {
        fun isWorkScheduled(context: Context, tag: String): Boolean {
            val statuses = WorkManager.getInstance(context).getWorkInfosByTag(tag)
            var runnning = false
            try {
                val workInfoList = statuses.get()
                workInfoList.forEach { info ->
                    if (info.state == WorkInfo.State.RUNNING ||
                        info.state == WorkInfo.State.ENQUEUED) runnning = true
                }

            } catch (e: Exception) {
                e.printStackTrace()
            }
            return runnning
        }
    }
}