package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupSafetyLabelTest {
  @Test fun labelsSafetyStates() { assertEquals("Backup protected", BackupSafetyState.SAFE.label()); assertEquals("Backup recommended", BackupSafetyState.DUE.label()); assertEquals("Backup needed", BackupSafetyState.OVERDUE.label()) }
}
