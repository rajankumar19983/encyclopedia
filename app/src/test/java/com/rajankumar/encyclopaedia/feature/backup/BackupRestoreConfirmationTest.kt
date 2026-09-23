package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreConfirmationTest {
  @Test fun confirmationNamesReplacement() = assertTrue(backupRestoreConfirmation.title.contains("Replace"))
  @Test fun confirmActionIsExplicit() = assertEquals("Restore backup", backupRestoreConfirmation.confirmLabel)
}
