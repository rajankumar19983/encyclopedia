package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Test

class DuplicateReviewGateTest {
  @Test fun duplicateDraftCannotBeMarkedReviewedByUniqueGate() {
    val draft = EditableImportDraft("Which memory is volatile?", listOf("RAM", "ROM"), "A")
    val queue = ImportReviewQueue(listOf(
      ImportReviewItem("one", draft),
      ImportReviewItem("two", draft)
    )).reviewIfValidAndUnique("one")
    assertFalse(queue.items.first { it.id == "one" }.reviewed)
  }
}
