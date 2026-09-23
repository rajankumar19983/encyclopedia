package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class OcrOptionCountTest {
  @Test fun acceptsVariableOptionCountsIncludingSix() {
    assertEquals(OcrOptionCountQuality.TOO_FEW, classifyOcrOptionCount(1))
    assertEquals(OcrOptionCountQuality.NORMAL, classifyOcrOptionCount(2))
    assertEquals(OcrOptionCountQuality.NORMAL, classifyOcrOptionCount(6))
    assertEquals(OcrOptionCountQuality.NORMAL, classifyOcrOptionCount(12))
    assertEquals(OcrOptionCountQuality.UNUSUALLY_MANY, classifyOcrOptionCount(13))
  }
}
