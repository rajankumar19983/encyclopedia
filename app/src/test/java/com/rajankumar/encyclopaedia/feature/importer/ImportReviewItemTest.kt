package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Test

class ImportReviewItemTest {
  @Test
  fun `converts drafts to unreviewed items`() {
    val items = listOf(EditableImportDraft("CPU?", listOf("A", "B"), "A")).toReviewItems()
    assertEquals("cpu-1", items.single().id)
    assertFalse(items.single().reviewed)
  }
}
