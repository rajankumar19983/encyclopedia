package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrReviewChecklistTest {
  @Test fun checklistCoversRequiredReviewPoints() {
    val checklist = ocrReviewChecklist()
    assertEquals(5, checklist.size)
    assertTrue(checklist.any { it.label.contains("Correct answer") && it.required })
    assertTrue(checklist.any { it.label.contains("Hindi/handwritten") && it.required })
  }
}
