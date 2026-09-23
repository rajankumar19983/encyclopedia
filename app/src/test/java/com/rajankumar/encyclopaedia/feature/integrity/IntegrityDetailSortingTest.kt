package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.assertEquals
import org.junit.Test

class IntegrityDetailSortingTest {
  @Test
  fun blockersSortBeforeWarnings() {
    val details = IntegrityReport(
      linkedSetOf(IntegrityIssue.QUESTION_TOPICS, IntegrityIssue.MANIFEST, IntegrityIssue.FIELDS),
      3
    ).sortedDetails()
    assertEquals(IntegritySeverity.ERROR, details.first().severity)
    assertEquals(IntegrityIssue.FIELDS, details[1].issue)
    assertEquals(IntegrityIssue.QUESTION_TOPICS, details[2].issue)
  }
}
