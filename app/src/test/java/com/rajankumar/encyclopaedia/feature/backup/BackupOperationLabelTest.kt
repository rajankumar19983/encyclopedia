package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupOperationLabelTest {
  @Test fun operationLabelsAreReadable() {
    assertEquals("Ready", BackupOperation.IDLE.label())
    assertEquals("Creating backup…", BackupOperation.CREATING.label())
    assertEquals("Restoring backup…", BackupOperation.RESTORING.label())
  }
}
