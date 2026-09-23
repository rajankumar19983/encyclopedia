package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertTrue
import org.junit.Test

class OcrImportReadinessMessageTest {
  @Test fun messageReportsAttentionCount() {
    val message = OcrImportReadinessSummary(4, 3, 1).message()
    assertTrue(message.contains("3 of 4"))
    assertTrue(message.contains("1 need attention"))
  }
}
