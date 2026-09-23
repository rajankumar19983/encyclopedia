package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.assertEquals
import org.junit.Test

class IntegrityExportTest {
  @Test
  fun exportItemsContainStableMachineFields() {
    val item = IntegrityReport(setOf(IntegrityIssue.IDS), 1).exportItems().single()
    assertEquals("ids", item.code)
    assertEquals("error", item.severity)
    assertEquals(IntegrityIssue.IDS.message(), item.message)
  }
}
