package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.*
import org.junit.Test

class IntegrityIssueDetailTest {
  @Test
  fun detailKeepsIssueSeverityAndReadableMessage() {
    val detail = IntegrityIssue.KNOWLEDGE_HIERARCHY.detail()
    assertEquals(IntegrityIssue.KNOWLEDGE_HIERARCHY, detail.issue)
    assertEquals(IntegritySeverity.ERROR, detail.severity)
    assertTrue(detail.message.contains("hierarchy", ignoreCase = true))
  }

  @Test
  fun everyIssueProducesNonBlankDetailMessage() {
    IntegrityIssue.entries.forEach { issue ->
      assertTrue(issue.detail().message.isNotBlank())
    }
  }
}
