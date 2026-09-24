package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotebookStrokeWidthTest {
  @Test fun acceptsSupportedStrokeWidths() {
    assertTrue(isValidStrokeWidth(1f))
    assertTrue(isValidStrokeWidth(32f))
  }

  @Test fun rejectsInvalidStrokeWidths() {
    assertFalse(isValidStrokeWidth(-2f))
    assertFalse(isValidStrokeWidth(Float.NaN))
  }
}
