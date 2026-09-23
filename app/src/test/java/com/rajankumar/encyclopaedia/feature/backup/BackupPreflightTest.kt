package com.rajankumar.encyclopaedia.feature.backup

import org.junit.Assert.*
import org.junit.Test

class BackupPreflightTest {
  private fun snapshot(version: Int) = BackupSnapshot(
    manifest = BackupManifest(
      formatVersion = version,
      createdAt = 1L,
      knowledgeNodeCount = 0,
      lessonCount = 0,
      questionCount = 0,
      questionTopicCount = 0,
      attemptCount = 0,
      plannerTaskCount = 0
    ),
    knowledgeNodes = emptyList(),
    lessons = emptyList(),
    questions = emptyList(),
    questionTopics = emptyList(),
    attempts = emptyList(),
    plannerTasks = emptyList()
  )

  @Test fun supportedBackupCanRestore() {
    val preflight = snapshot(BACKUP_FORMAT_VERSION).preflight()
    assertEquals(BackupCompatibility.SUPPORTED, preflight.compatibility)
    assertTrue(preflight.canRestore)
    assertTrue(preflight.canInspect)
  }

  @Test fun newerBackupCannotRestore() {
    assertFalse(snapshot(BACKUP_FORMAT_VERSION + 1).preflight().canRestore)
  }

  @Test fun olderUnsupportedBackupCannotRestore() {
    assertFalse(snapshot(MIN_SUPPORTED_BACKUP_FORMAT_VERSION - 1).preflight().canRestore)
  }
}
