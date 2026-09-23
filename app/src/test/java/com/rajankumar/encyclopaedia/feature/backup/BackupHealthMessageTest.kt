package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertTrue
import org.junit.Test

class BackupHealthMessageTest {
  @Test fun messagesAreActionable() {
    assertTrue(BackupHealth.NO_BACKUP.message().contains("first backup"))
    assertTrue(BackupHealth.NEEDS_BACKUP.message().contains("fresh backup"))
    assertTrue(BackupHealth.HEALTHY.message().contains("recovery point"))
  }
}
