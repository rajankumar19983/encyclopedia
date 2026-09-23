package com.rajankumar.encyclopaedia.feature.backup

/**
 * Chooses restore points to keep after a newly-created backup has already been
 * written and validated. Callers must never delete an older valid backup until
 * the replacement backup is known to be valid.
 */
object BackupRetention {
  fun keepLatest(
    restorePoints: List<BackupRestorePoint>,
    limit: Int = MAX_RESTORE_POINTS
  ): List<BackupRestorePoint> = restorePoints
    .filter(BackupRestorePoint::valid)
    .sortedByDescending(BackupRestorePoint::createdAt)
    .take(limit.coerceAtLeast(1))

  fun removableAfterSuccessfulBackup(
    restorePoints: List<BackupRestorePoint>,
    limit: Int = MAX_RESTORE_POINTS
  ): List<BackupRestorePoint> {
    val keep = keepLatest(restorePoints, limit).map(BackupRestorePoint::name).toSet()
    return restorePoints.filter { it.valid && it.name !in keep }
  }
}
