package com.rajankumar.encyclopaedia.feature.notebook

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class NotebookPageGeometryTest {
  @Test fun acceptsNormalPageSize() = assertTrue(isValidNotebookPageSize(1600f, 2200f))
  @Test fun rejectsTinyPage() = assertFalse(isValidNotebookPageSize(100f, 100f))
  @Test fun rejectsHugePage() = assertFalse(isValidNotebookPageSize(9000f, 13000f))
  @Test fun rejectsNonFinitePage() = assertFalse(isValidNotebookPageSize(Float.NaN, 2200f))
}
