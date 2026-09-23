package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertTrue
import org.junit.Test

class ImportReviewQueueTest {
  @Test
  fun `marks matching review item complete`() {
    val item = ImportReviewItem("q-1", EditableImportDraft("Q", listOf("A", "B"), "A"))
    val queue = ImportReviewQueue(listOf(item)).markReviewed("q-1")
    assertEquals(0, queue.remaining)
    assertTrue(queue.complete)
  }
}
