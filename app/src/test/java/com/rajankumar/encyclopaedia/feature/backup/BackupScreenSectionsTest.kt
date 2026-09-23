package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Test

class BackupScreenSectionsTest {
  @Test fun screenHasStatusActionsAndSafety() = assertEquals(
    listOf("Recovery status", "Backup actions", "Before restoring"),
    BackupScreenSection.entries.map { it.title }
  )
}
