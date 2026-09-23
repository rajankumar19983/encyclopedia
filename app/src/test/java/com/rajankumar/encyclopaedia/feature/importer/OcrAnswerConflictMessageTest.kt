package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrAnswerConflictMessageTest {
  @Test fun cleanAnswersNeedNoWarning() = assertNull(OcrAnswerConflict(listOf("A", "A")).warning())

  @Test fun conflictWarningNamesCandidates() {
    val warning = OcrAnswerConflict(listOf("A", "C")).warning().orEmpty()
    assertTrue(warning.contains("A, C"))
  }
}
