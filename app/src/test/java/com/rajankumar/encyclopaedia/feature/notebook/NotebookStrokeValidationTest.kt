package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotebookStrokeValidationTest {
  @Test fun acceptsNormalStrokeWidth() = assertTrue(isValidStrokeWidth(4f))
  @Test fun rejectsZeroStrokeWidth() = assertFalse(isValidStrokeWidth(0f))
  @Test fun rejectsNonFiniteStrokeWidth() = assertFalse(isValidStrokeWidth(Float.POSITIVE_INFINITY))
  @Test fun acceptsKnownToolsIgnoringCase() = assertTrue(isSupportedStrokeTool(" highlighter "))
  @Test fun rejectsUnknownTool() = assertFalse(isSupportedStrokeTool("spray"))
}
