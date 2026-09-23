package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreReviewPresentationTest {
  @Test fun cleanPresentationOffersRestore() {
    val inspection = BackupInspection(
      BackupPreflight(BackupCompatibility.SUPPORTED, 8, true, true),
      IntegrityReport(emptySet(), 8)
    )
    val presentation = inspection.restoreReviewPresentation()
    assertEquals("Backup is ready to restore", presentation.headline)
    assertEquals("Restore backup", presentation.actionLabel)
    assertTrue(presentation.actionEnabled)
  }
}
