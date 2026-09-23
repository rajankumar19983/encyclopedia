package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssue
import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreIssueGroupsTest {
  private fun inspection(issues: Set<IntegrityIssue>) = BackupInspection(
    preflight = BackupPreflight(BackupCompatibility.SUPPORTED, 7, true, true),
    integrity = IntegrityReport(issues, 7)
  )

  @Test
  fun groupsIssuesIntoRestoreReviewSections() {
    val groups = inspection(
      setOf(
        IntegrityIssue.IDS,
        IntegrityIssue.FIELDS,
        IntegrityIssue.QUESTION_TOPICS,
        IntegrityIssue.NOTEBOOK_RELATIONSHIPS
      )
    ).restoreIssueGroups()

    assertEquals(
      listOf(
        BackupRestoreIssueGroup.BACKUP_STRUCTURE,
        BackupRestoreIssueGroup.CONTENT,
        BackupRestoreIssueGroup.RELATIONSHIPS
      ),
      groups.map { it.group }
    )
    assertEquals(listOf(1, 1, 2), groups.map { it.issueCount })
  }

  @Test
  fun healthyInspectionHasNoIssueGroups() {
    assertTrue(inspection(emptySet()).restoreIssueGroups().isEmpty())
  }

  @Test
  fun unavailableIntegrityReportHasNoIssueGroups() {
    val inspection = BackupInspection(
      preflight = BackupPreflight(BackupCompatibility.TOO_NEW, 0, false, false),
      integrity = null
    )

    assertTrue(inspection.restoreIssueGroups().isEmpty())
  }
}
