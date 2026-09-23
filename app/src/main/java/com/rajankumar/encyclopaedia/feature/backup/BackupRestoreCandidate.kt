package com.rajankumar.encyclopaedia.feature.backup

import android.net.Uri

data class BackupRestoreCandidate(
  val point: BackupRestorePoint,
  val uri: Uri,
  val snapshot: BackupSnapshot,
  val inspection: BackupInspection
) {
  fun session(warningsAcknowledged: Boolean = false): BackupRestoreReviewSession =
    inspection.restoreReviewSession(warningsAcknowledged)

  fun presentation(warningsAcknowledged: Boolean = false): BackupRestoreReviewSessionPresentation =
    inspection.restoreReviewSessionPresentation(warningsAcknowledged)
}
