package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.feature.backup.BackupManifest
import com.rajankumar.encyclopaedia.feature.backup.BackupSnapshot
import org.junit.Assert.assertTrue
import org.junit.Test

class RestoreGateTest {
  private fun manifest(questionCount: Int = 0) = BackupManifest(
    createdAt = 1L,
    knowledgeNodeCount = 0,
    lessonCount = 0,
    questionCount = questionCount,
    questionTopicCount = 0,
    attemptCount = 0,
    plannerTaskCount = 0
  )

  private fun snapshot(manifest: BackupManifest) = BackupSnapshot(
    manifest = manifest,
    knowledgeNodes = emptyList(),
    lessons = emptyList(),
    questions = emptyList(),
    questionTopics = emptyList(),
    attempts = emptyList(),
    plannerTasks = emptyList()
  )

  @Test
  fun acceptsEmptyConsistentSnapshot() {
    assertTrue(snapshot(manifest()).passesRestoreIntegrityGate())
  }

  @Test(expected = IllegalArgumentException::class)
  fun requireRejectsInvalidManifest() {
    snapshot(manifest(questionCount = 1)).requireRestoreIntegrity()
  }
}
