package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.assertEquals
import org.junit.Test

class IntegrityStatusTest {
  @Test
  fun reportsHealthyWhenThereAreNoIssues() = assertEquals(
    IntegrityStatus.HEALTHY,
    IntegrityReport(emptySet(), 0).status
  )

  @Test
  fun reportsWarningForNonBlockingIssues() = assertEquals(
    IntegrityStatus.WARNING,
    IntegrityReport(setOf(IntegrityIssue.FIELDS), 1).status
  )

  @Test
  fun reportsBlockedWhenAnyErrorExists() = assertEquals(
    IntegrityStatus.BLOCKED,
    IntegrityReport(setOf(IntegrityIssue.FIELDS, IntegrityIssue.IDS), 2).status
  )
}
