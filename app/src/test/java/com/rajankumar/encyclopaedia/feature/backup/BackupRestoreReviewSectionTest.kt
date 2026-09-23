package com.rajankumar.encyclopaedia.feature.backup

import com.rajankumar.encyclopaedia.feature.integrity.IntegrityIssue
import com.rajankumar.encyclopaedia.feature.integrity.IntegrityReport
import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class BackupRestoreReviewSectionTest {
  private fun inspection(issues: Set<IntegrityIssue>) = BackupInspection(
    preflight = BackupPreflight(BackupCompatibility.SUPPORTED, 8, true, true),
    integrity = IntegrityReport(issues, 8)
  )

  @Test
  fun sectionsCountErrorsAndWarningsWithinEachGroup() {
    val sections = inspection(
      setOf(
        IntegrityIssue.IDS,
        IntegrityIssue.FIELDS,
        IntegrityIssue.QUESTION_TOPICS,
        IntegrityIssue.STUDY_RELATIONSHIPS
      )
    ).restoreReviewSections()

    assertEquals(3, sections.size)
    assertEquals(1, sections[0].errorCount)
    assertTrue(sections[0].hasBlockingIssues)
    assertEquals(1, sections[1].warningCount)
    assertFalse(sections[1].hasBlockingIssues)
    assertEquals(1, sections[2].errorCount)
    assertEquals(1, sections[2].warningCount)
    assertTrue(sections[2].hasBlockingIssues)
  }

  @Test
  fun sectionMessagesComeFromIntegrityDetails() {
    val section = inspection(setOf(IntegrityIssue.FIELDS)).restoreReviewSections().single()
    assertEquals(BackupRestoreIssueGroup.CONTENT, section.group)
    assertEquals(1, section.messages.size)
    assertTrue(section.messages.single().isNotBlank())
  }
}
