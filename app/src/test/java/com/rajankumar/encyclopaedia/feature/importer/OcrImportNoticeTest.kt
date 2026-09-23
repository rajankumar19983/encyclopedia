package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertTrue
import org.junit.Test

class OcrImportNoticeTest {
  @Test fun sanitizationIsVisibleToReviewer() {
    val session = prepareOcrImportSession("1. Which is RAM? प्रश्न\nA. Memory\nB. Storage\nAnswer: A")
    assertTrue(session.notices().any { it.message.contains("removed", ignoreCase = true) })
  }
}
