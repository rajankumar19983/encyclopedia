package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupProgressLabelTest {
  @Test fun emptyProgressIsPreparing() = assertEquals("Preparing backup", BackupProgress(0, 0).label())
  @Test fun activeProgressIncludesCounts() = assertEquals("2 of 4 items • 50%", BackupProgress(2, 4).label())
}
