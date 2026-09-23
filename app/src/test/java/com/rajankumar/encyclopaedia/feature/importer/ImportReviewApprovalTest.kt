package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ImportReviewApprovalTest {
  @Test fun unreviewedDraftCannotPersist() {
    val draft = EditableImportDraft("Which memory is volatile?", listOf("RAM", "ROM"), "A")
    val queue = ImportReviewQueue(listOf(ImportReviewItem("q1", draft)))
    assertFalse(queue.canPersistAll())
    assertTrue(queue.persistableDrafts().isEmpty())
  }

  @Test fun validReviewedDraftCanPersist() {
    val draft = EditableImportDraft("Which memory is volatile?", listOf("RAM", "ROM"), "A")
    val queue = ImportReviewQueue(listOf(ImportReviewItem("q1", draft))).reviewIfValid("q1")
    assertTrue(queue.canPersistAll())
  }
}
