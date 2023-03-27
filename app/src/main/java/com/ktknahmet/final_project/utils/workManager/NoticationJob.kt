package com.ktknahmet.final_project.utils.workManager

import android.content.Context

import androidx.work.Worker
import androidx.work.WorkerParameters

class NoticationJob(val context: Context, workerParams: WorkerParameters) : Worker(context,
    workerParams
) {
    override fun doWork(): Result {
        return Result.success()
    }
}
