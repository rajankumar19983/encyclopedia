package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class ImportSavePlanTest {
  @Test
  fun `separates valid and blocked selected items`() {
    val valid = ImportReviewItem("valid", EditableImportDraft("Question?", listOf("Yes", "No"), "A"))
    val invalid = ImportReviewItem("invalid", EditableImportDraft("", listOf("Yes"), "A"))
    val plan = buildImportSavePlan(
      listOf(valid, invalid),
      ImportSaveSelection(setOf("valid", "invalid")),
    )
    assertEquals(listOf("valid"), plan.ready.map { it.id })
    assertEquals(listOf("invalid"), plan.blocked.map { it.id })
  }
}
