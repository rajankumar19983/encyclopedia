package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class OcrQuestionLengthTest {
  @Test fun classifiesQuestionLengths() {
    assertEquals(OcrQuestionLengthQuality.TOO_SHORT, classifyOcrQuestionLength("Q?"))
    assertEquals(OcrQuestionLengthQuality.NORMAL, classifyOcrQuestionLength("Which protocol uses port 53?"))
    assertEquals(OcrQuestionLengthQuality.VERY_LONG, classifyOcrQuestionLength("x".repeat(2001)))
  }
}
