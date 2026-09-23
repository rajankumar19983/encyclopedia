package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrSanitizerTest {
  @Test fun removesLanguageAndNoiseLinesWhileReportingThem() {
    val result = sanitizeOcrText("1. What is DNS?\nउत्तर डीएनएस\n@@@ ### /// ???\nA. Domain Name System")
    assertTrue(result.text.contains("What is DNS"))
    assertTrue(result.text.contains("Domain Name System"))
    assertFalse(result.text.contains("उत्तर"))
    assertEquals(2, result.removedLines.size)
  }
}
