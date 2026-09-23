package com.rajankumar.encyclopaedia.feature.backup

data class BackupRestoreReview(
  val readiness: BackupRestoreReadiness,
  val restoreEnabled: Boolean,
  val title: String,
  val message: String
)

fun BackupInspection.restoreReview(): BackupRestoreReview {
  val readiness = restoreReadiness()
  return BackupRestoreReview(
    readiness = readiness,
    restoreEnabled = canRestore,
    title = when (readiness) {
      BackupRestoreReadiness.READY -> "Backup ready to restore"
      BackupRestoreReadiness.REVIEW_WARNINGS -> "Review backup warnings"
      BackupRestoreReadiness.BLOCKED_BY_COMPATIBILITY -> "Backup format unsupported"
      BackupRestoreReadiness.BLOCKED_BY_INTEGRITY -> "Backup integrity check failed"
    },
    message = when (readiness) {
      BackupRestoreReadiness.READY -> "No blocking backup problems were found."
      BackupRestoreReadiness.REVIEW_WARNINGS -> "Warnings were found. Review them before restoring."
      BackupRestoreReadiness.BLOCKED_BY_COMPATIBILITY -> preflight.compatibility.description()
      BackupRestoreReadiness.BLOCKED_BY_INTEGRITY -> "Restore is blocked because the backup contains integrity errors."
    }
  )
}
