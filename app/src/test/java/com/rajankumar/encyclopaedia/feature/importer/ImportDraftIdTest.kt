package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class ImportDraftIdTest {
  @Test
  fun `builds readable stable id`() {
    assertEquals("what-is-ram-2", importDraftId(" What is RAM? ", 1))
  }

  @Test
  fun `falls back for non latin question`() {
    assertEquals("question-1", importDraftId("???", 0))
  }
}
