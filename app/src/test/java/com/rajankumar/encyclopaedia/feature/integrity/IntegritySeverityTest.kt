package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.*
import org.junit.Test

class IntegritySeverityTest {
  @Test
  fun structuralIssuesAreErrors() {
    val errors = listOf(
      IntegrityIssue.MANIFEST,
      IntegrityIssue.IDS,
      IntegrityIssue.KNOWLEDGE_HIERARCHY,
      IntegrityIssue.STUDY_RELATIONSHIPS,
      IntegrityIssue.NOTEBOOK_RELATIONSHIPS
    )
    errors.forEach { assertEquals(IntegritySeverity.ERROR, it.severity()) }
  }

  @Test
  fun repairableContentIssuesAreWarnings() {
    assertEquals(IntegritySeverity.WARNING, IntegrityIssue.FIELDS.severity())
    assertEquals(IntegritySeverity.WARNING, IntegrityIssue.QUESTION_TOPICS.severity())
  }
}
