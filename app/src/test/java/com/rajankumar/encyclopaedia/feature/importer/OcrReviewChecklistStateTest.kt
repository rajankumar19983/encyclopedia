package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrReviewChecklistStateTest {
  @Test fun optionalItemDoesNotBlockCompletion() {
    val items = ocrReviewChecklist()
    var state = OcrReviewChecklistState()
    items.indices.filter { items[it].required }.forEach { state = state.toggle(it) }
    assertTrue(state.requiredComplete(items))
  }

  @Test fun uncheckedRequiredItemBlocksCompletion() {
    assertFalse(OcrReviewChecklistState().requiredComplete(ocrReviewChecklist()))
  }
}
