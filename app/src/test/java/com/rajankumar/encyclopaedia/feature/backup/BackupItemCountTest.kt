package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupItemCountTest {
  @Test fun labelsItemCounts() { assertEquals("No items", backupItemCountLabel(0)); assertEquals("1 item", backupItemCountLabel(1)); assertEquals("12 items", backupItemCountLabel(12)) }
}
