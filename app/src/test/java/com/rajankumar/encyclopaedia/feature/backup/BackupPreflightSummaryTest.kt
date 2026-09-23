package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertTrue
import org.junit.Test

class BackupPreflightSummaryTest {
  @Test fun supportedSummaryIncludesRecordCount() {
    assertTrue(BackupPreflight(BackupCompatibility.SUPPORTED, 9, true, true).summary().contains("9 records"))
  }

  @Test fun blockedSummaryExplainsReason() {
    assertTrue(BackupPreflight(BackupCompatibility.TOO_OLD, 9, true, false).summary().contains("blocked"))
  }
}
