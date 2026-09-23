package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class OcrSaveStatusMessageTest {
  @Test fun statusesHaveActionableLabels() {
    assertEquals("Approve & Save", OcrDraftSaveStatus.READY.label())
    assertEquals("Already exists", OcrDraftSaveStatus.DUPLICATE.label())
    assertEquals("Retry Save", OcrDraftSaveStatus.FAILED.label())
  }
}
