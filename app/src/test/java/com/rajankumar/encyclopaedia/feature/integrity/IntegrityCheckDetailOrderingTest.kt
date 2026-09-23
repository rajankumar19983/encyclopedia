package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class IntegrityCheckDetailOrderingTest {
  @Test fun blockingDetailsAppearBeforeWarnings() {
    val presentation = IntegrityReport(
      setOf(IntegrityIssue.FIELDS, IntegrityIssue.STUDY_RELATIONSHIPS, IntegrityIssue.IDS),
      12
    ).toCheckPresentation()
    assertEquals(IntegritySeverity.ERROR, presentation.details.first().severity)
    assertTrue(presentation.details.dropWhile { it.severity == IntegritySeverity.ERROR }.all { it.severity == IntegritySeverity.WARNING })
  }
}
