package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupEmptyStateTest {
  @Test fun emptyStateHasDirectAction() = assertEquals("Create backup", backupEmptyState.action)
  @Test fun emptyStateExplainsRecoveryPoint() = assertTrue(backupEmptyState.title.contains("recovery point"))
}
