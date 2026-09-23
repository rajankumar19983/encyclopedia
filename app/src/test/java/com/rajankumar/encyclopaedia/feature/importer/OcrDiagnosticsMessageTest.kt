package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertNull
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrDiagnosticsMessageTest {
  @Test fun cleanBatchHasNoDiagnosticMessage() = assertNull(OcrBatchDiagnostics(2, 2, emptyList(), emptyList()).message())

  @Test fun diagnosticMessageExplainsAllDetectedProblems() {
    val message = OcrBatchDiagnostics(4, 3, listOf(12), listOf(11)).message().orEmpty()
    assertTrue(message.contains("produced 3 review drafts"))
    assertTrue(message.contains("Missing question numbers: 12"))
    assertTrue(message.contains("Duplicate question numbers: 11"))
  }
}
