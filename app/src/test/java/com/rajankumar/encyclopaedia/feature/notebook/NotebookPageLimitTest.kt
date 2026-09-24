package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertNotNull
import org.junit.Assert.assertNull
import org.junit.Test

class NotebookPageLimitTest {
  @Test fun ordinaryPageHasNoWarning() = assertNull(notebookStrokeWarning(200))
  @Test fun detailedPageSuggestsNewPage() = assertNotNull(notebookStrokeWarning(5_000))
}
