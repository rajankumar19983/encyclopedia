package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertEquals
import org.junit.Test

class NotebookStrokeWidthTest {
  @Test fun strokeWidthIsBounded() { assertEquals(1f, sanitizeStrokeWidth(-2f)); assertEquals(32f, sanitizeStrokeWidth(100f)) }
  @Test fun invalidStrokeWidthUsesDefault() = assertEquals(4f, sanitizeStrokeWidth(Float.NaN))
}
