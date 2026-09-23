package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrVariableOptionsRegressionTest {
  @Test fun parserKeepsSixPrintedOptions() {
    val raw = "1. Choose the valid item?\nA. One\nB. Two\nC. Three\nD. Four\nE. Five\nF. Six\nAnswer: F"
    val draft = prepareOcrImport(raw).drafts.single()
    assertEquals(6, draft.options.size)
    assertEquals("F", draft.correctAnswer)
    assertTrue(draft.warnings.none { it.contains("at least 2") })
  }
}
