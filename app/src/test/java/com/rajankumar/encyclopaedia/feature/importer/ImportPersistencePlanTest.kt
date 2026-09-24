package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ImportPersistencePlanTest {
  @Test fun reviewedDuplicatesStillCannotPersist() {
    val draft = EditableImportDraft("Which memory is volatile?", listOf("RAM", "ROM"), "A")
    val queue = ImportReviewQueue(listOf(
      ImportReviewItem("one", draft, true),
      ImportReviewItem("two", draft, true)
    ))
    assertFalse(queue.persistencePlan().ready)
    assertTrue(queue.persistencePlan().drafts.isEmpty())
  }

  @Test fun reviewedUniqueQuestionsCanPersist() {
    val draft = EditableImportDraft("Which memory is volatile?", listOf("RAM", "ROM"), "A")
    val queue = ImportReviewQueue(listOf(ImportReviewItem("one", draft, true)))
    assertTrue(queue.persistencePlan().ready)
  }
}
