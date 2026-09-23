package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupDestinationLabelTest {
  @Test fun labelsAreReadable() {
    assertEquals("Device storage", BackupDestination.DEVICE.label())
    assertEquals("Google Drive", BackupDestination.GOOGLE_DRIVE.label())
    assertEquals("Automatic", BackupType.AUTOMATIC.label())
    assertEquals("Manual", BackupType.MANUAL.label())
  }
}
