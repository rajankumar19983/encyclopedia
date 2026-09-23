package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.assertTrue
import org.junit.Test

class IntegrityIssueMessageTest {
  @Test fun everyIssueHasReadableMessage() {
    IntegrityIssue.entries.forEach { issue ->
      assertTrue(issue.message().isNotBlank())
      assertTrue(issue.message().endsWith("."))
    }
  }
}
