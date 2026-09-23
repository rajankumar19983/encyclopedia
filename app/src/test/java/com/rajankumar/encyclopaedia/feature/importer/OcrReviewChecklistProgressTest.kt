package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrReviewChecklistProgressTest {
  @Test fun countsOnlyRequiredItems() {
    val items = ocrReviewChecklist()
    var state = OcrReviewChecklistState()
    items.indices.filter { items[it].required }.take(2).forEach { state = state.toggle(it) }
    val progress = state.progress(items)
    assertEquals(4, progress.required)
    assertEquals(2, progress.completed)
    assertFalse(progress.complete)
  }

  @Test fun completeWhenRequiredItemsChecked() {
    val items = ocrReviewChecklist()
    var state = OcrReviewChecklistState()
    items.indices.filter { items[it].required }.forEach { state = state.toggle(it) }
    assertTrue(state.progress(items).complete)
  }
}
