package com.rajankumar.encyclopaedia.feature.integrity

import org.junit.Assert.assertEquals
import org.junit.Test

class IntegrityIssueCodeTest {
  @Test
  fun issueCodesAreStableLowercaseNames() {
    assertEquals("manifest", IntegrityIssue.MANIFEST.code())
    assertEquals("study_relationships", IntegrityIssue.STUDY_RELATIONSHIPS.code())
  }
}
