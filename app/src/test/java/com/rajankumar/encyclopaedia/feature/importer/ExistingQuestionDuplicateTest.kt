package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ExistingQuestionDuplicateTest {
  @Test fun detectsQuestionAlreadyPresentInStudyBank() {
    val draft = EditableImportDraft("What does CPU stand for?", listOf("Central Processing Unit", "Computer Personal Unit"), "A")
    val queue = ImportReviewQueue(listOf(ImportReviewItem("import-1", draft)))
    val existing = ExistingQuestionFingerprint("stored-9", "What does CPU stand for", listOf("central processing unit", "computer personal unit"))
    val matches = queue.findExistingDuplicates(listOf(existing))
    assertEquals(1, matches.size)
    assertEquals("stored-9", matches.single().existingQuestionId)
    assertTrue(queue.auditDuplicates(listOf(existing)).hasDuplicates)
  }
}
