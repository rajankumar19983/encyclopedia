package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupProgressTest {
  @Test fun calculatesPercent() = assertEquals(50, BackupProgress(5, 10).percent)
  @Test fun clampsInvalidCounts() { assertEquals(0, BackupProgress(-2, 10).percent); assertEquals(100, BackupProgress(20, 10).percent) }
}
