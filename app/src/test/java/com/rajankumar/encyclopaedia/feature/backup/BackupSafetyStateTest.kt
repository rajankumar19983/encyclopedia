package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupSafetyStateTest {
  @Test fun mapsAgeToSafety() { assertEquals(BackupSafetyState.SAFE, BackupAge.TODAY.safetyState()); assertEquals(BackupSafetyState.DUE, BackupAge.RECENT.safetyState()); assertEquals(BackupSafetyState.OVERDUE, BackupAge.STALE.safetyState()) }
}
