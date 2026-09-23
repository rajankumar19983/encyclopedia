package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupAgeTest {
  @Test fun classifiesBackupAge() {
    val day = 86_400_000L
    assertEquals(BackupAge.TODAY, backupAge(10 * day, 10 * day))
    assertEquals(BackupAge.RECENT, backupAge(5 * day, 10 * day))
    assertEquals(BackupAge.STALE, backupAge(1 * day, 10 * day))
  }
}
