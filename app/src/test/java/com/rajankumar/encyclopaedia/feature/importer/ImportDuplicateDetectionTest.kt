package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ImportDuplicateDetectionTest {
  @Test fun duplicateQuestionsInsideOneImportAreGrouped() {
    val first = EditableImportDraft("Which memory is volatile?", listOf("RAM", "ROM"), "A")
    val second = EditableImportDraft("Which memory is volatile", listOf("ram", "rom"), "A")
    val queue = ImportReviewQueue(listOf(ImportReviewItem("one", first), ImportReviewItem("two", second)))
    val groups = queue.duplicateGroups()
    assertEquals(1, groups.size)
    assertEquals(setOf("one", "two"), groups.single().itemIds.toSet())
  }

  @Test fun uniqueQuestionsHaveNoDuplicateIds() {
    val queue = ImportReviewQueue(listOf(
      ImportReviewItem("one", EditableImportDraft("Question one", listOf("A", "B"), "A")),
      ImportReviewItem("two", EditableImportDraft("Question two", listOf("A", "B"), "A"))
    ))
    assertTrue(queue.duplicateItemIds().isEmpty())
  }
}
