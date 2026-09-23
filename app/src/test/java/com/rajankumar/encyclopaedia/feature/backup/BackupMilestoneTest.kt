package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertTrue
import org.junit.Test

class BackupMilestoneTest {
  @Test fun localBackupSafetyMilestoneIsComplete() = assertTrue(currentBackupMilestone.complete)
}
