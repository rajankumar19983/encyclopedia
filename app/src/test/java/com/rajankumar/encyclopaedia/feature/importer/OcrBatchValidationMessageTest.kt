package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertTrue
import org.junit.Test

class OcrBatchValidationMessageTest {
  @Test fun messageReportsFlaggedAndDuplicates() {
    val message = OcrBatchValidation(5, 3, 2, 1).message()
    assertTrue(message.contains("3 of 5"))
    assertTrue(message.contains("2 flagged"))
    assertTrue(message.contains("1 possible duplicate group"))
  }
}
