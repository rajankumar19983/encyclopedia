package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupRestoreReviewStatusLabelTest {
  @Test fun presentationMapsToClearStatus() {
    assertEquals("Ready to restore", BackupRestoreReviewSessionPresentation("", "", emptyList(), "", true, false).reviewStatusLabel())
    assertEquals("Review required", BackupRestoreReviewSessionPresentation("", "", emptyList(), "", false, true).reviewStatusLabel())
    assertEquals("Restore blocked", BackupRestoreReviewSessionPresentation("", "", emptyList(), "", false, false).reviewStatusLabel())
  }
}
