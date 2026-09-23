package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrAnswerConflictTest {
  @Test fun repeatedSameAnswerIsNotConflict() = assertFalse(
    detectOcrAnswerConflict(listOf("Answer: B", "Correct option: 2")).hasConflict,
  )

  @Test fun differentAnswerLinesAreConflict() = assertTrue(
    detectOcrAnswerConflict(listOf("Answer: A", "Correct answer: C")).hasConflict,
  )
}
