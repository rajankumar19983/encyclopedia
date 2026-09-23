package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class IntegrityCheckItemTest {
  @Test fun presentationAddsActionableMetadata() {
    val item = IntegrityReport(setOf(IntegrityIssue.STUDY_RELATIONSHIPS), 3)
      .toCheckPresentation()
      .items
      .single()

    assertEquals("Study relationships", item.label)
    assertEquals(IntegritySeverity.ERROR, item.severity)
    assertTrue(item.guidance.contains("Repair", ignoreCase = true))
  }
}
