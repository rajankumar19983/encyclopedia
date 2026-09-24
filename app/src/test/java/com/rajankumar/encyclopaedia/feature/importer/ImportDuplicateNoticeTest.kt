package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class ImportDuplicateNoticeTest {
  @Test fun duplicateAuditExplainsBothDuplicateSources() {
    val draft = EditableImportDraft("What is RAM?", listOf("Memory", "Storage"), "A")
    val queue = ImportReviewQueue(listOf(
      ImportReviewItem("one", draft),
      ImportReviewItem("two", draft)
    ))
    val existing = ExistingQuestionFingerprint("stored", "What is RAM", listOf("Memory", "Storage"))
    assertEquals(2, queue.auditDuplicates(listOf(existing)).notices().size)
  }
}
