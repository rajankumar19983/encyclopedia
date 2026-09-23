package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.*
import org.junit.Test

class RestoreIntegrityDecisionTest {
  @Test
  fun warningsDoNotBlockRestore() {
    val decision = IntegrityReport(setOf(IntegrityIssue.FIELDS), 1).restoreDecision()
    assertTrue(decision.allowed)
    assertEquals(1, decision.warnings.size)
    assertTrue(decision.blockers.isEmpty())
  }

  @Test
  fun structuralErrorBlocksRestore() {
    val decision = IntegrityReport(
      setOf(IntegrityIssue.IDS, IntegrityIssue.FIELDS),
      2
    ).restoreDecision()
    assertFalse(decision.allowed)
    assertEquals(1, decision.blockers.size)
    assertEquals(1, decision.warnings.size)
  }
}
