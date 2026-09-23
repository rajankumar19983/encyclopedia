package com.rajankumar.encyclopaedia.feature.backup

import android.content.Context
import androidx.work.CoroutineWorker
import androidx.work.WorkerParameters
import com.rajankumar.encyclopaedia.data.local.EncyclopaediaDatabase

class AutomaticBackupWorker(
  appContext: Context,
  workerParams: WorkerParameters
) : CoroutineWorker(appContext, workerParams) {
  override suspend fun doWork(): Result {
    val store = DeviceBackupStore(applicationContext)
    if (store.configuredDirectory() == null) return Result.success()

    return runCatching {
      val dao = EncyclopaediaDatabase.get(applicationContext).dao()
      val snapshot = createBackupSnapshot(dao)
      DeviceBackupManager(store).create(snapshot, BackupType.AUTOMATIC)
    }.fold(
      onSuccess = { Result.success() },
      onFailure = { Result.retry() }
    )
  }
}
