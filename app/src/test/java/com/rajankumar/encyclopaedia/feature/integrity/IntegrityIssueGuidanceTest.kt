package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.assertTrue
import org.junit.Test

class IntegrityIssueGuidanceTest {
  @Test fun everyIssueHasReadableLabelAndGuidance() {
    IntegrityIssue.entries.forEach { issue ->
      assertTrue(issue.label().isNotBlank())
      assertTrue(issue.guidance().isNotBlank())
      assertTrue(issue.guidance().endsWith("."))
    }
  }
}
