package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.*
import org.junit.Test

class IntegrityPresentationTest {
  @Test
  fun presentationIsReadyForUiConsumption() {
    val presentation = IntegrityReport(setOf(IntegrityIssue.IDS), 7).presentation()
    assertEquals("Backup cannot be safely restored", presentation.headline)
    assertTrue(presentation.summary.contains("7 records"))
    assertEquals(IntegrityIssue.IDS.message(), presentation.primaryMessage)
  }
}
