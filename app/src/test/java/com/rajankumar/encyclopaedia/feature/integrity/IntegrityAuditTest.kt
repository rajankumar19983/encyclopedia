package com.rajankumar.encyclopaedia.feature.integrity

import com.rajankumar.encyclopaedia.feature.backup.*
import org.junit.Assert.*
import org.junit.Test

class IntegrityAuditTest {
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
  fun cleanEmptySnapshotHasNoIssues() = assertTrue(
    snapshot(manifest()).integrityIssues().isEmpty()
  )

  @Test
  fun countMismatchReportsManifestIssue() = assertTrue(
    IntegrityIssue.MANIFEST in snapshot(manifest(questionCount = 1)).integrityIssues()
  )
}
