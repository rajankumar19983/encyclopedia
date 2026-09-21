package com.rajankumar.encyclopaedia.feature.backup

import android.content.Context
import androidx.work.ExistingPeriodicWorkPolicy
import androidx.work.PeriodicWorkRequestBuilder
import androidx.work.WorkManager
import java.time.Duration
import java.time.ZonedDateTime
import java.util.concurrent.TimeUnit

object AutomaticBackupScheduler {
  private const val UNIQUE_WORK_NAME = "encyclopaedia_daily_backup"

  fun schedule(context: Context) {
    val now = ZonedDateTime.now()
    var nextMidnight = now.toLocalDate().plusDays(1).atStartOfDay(now.zone)
    if (!nextMidnight.isAfter(now)) nextMidnight = nextMidnight.plusDays(1)
    val delayMillis = Duration.between(now, nextMidnight).toMillis().coerceAtLeast(0L)

    val request = PeriodicWorkRequestBuilder<AutomaticBackupWorker>(24, TimeUnit.HOURS)
      .setInitialDelay(delayMillis, TimeUnit.MILLISECONDS)
      .build()

    WorkManager.getInstance(context.applicationContext).enqueueUniquePeriodicWork(
      UNIQUE_WORK_NAME,
      ExistingPeriodicWorkPolicy.UPDATE,
      request
    )
  }

  fun cancel(context: Context) {
    WorkManager.getInstance(context.applicationContext).cancelUniqueWork(UNIQUE_WORK_NAME)
  }
}
