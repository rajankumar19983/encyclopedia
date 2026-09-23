package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ImportReviewEditTest {
  @Test fun editingResetsReviewAndRemovesDevanagari() {
    val original = EditableImportDraft("Original question text", listOf("One", "Two"), "A")
    val reviewed = ImportReviewQueue(listOf(ImportReviewItem("q1", original, true)))
    val updated = reviewed.updateDraft("q1", EditableImportDraft("Updated प्रश्न text", listOf("One एक", "Two"), "A"))
    assertFalse(updated.items.single().reviewed)
    assertTrue(updated.items.single().draft.hasOnlyEnglishOcrContent())
  }
}
