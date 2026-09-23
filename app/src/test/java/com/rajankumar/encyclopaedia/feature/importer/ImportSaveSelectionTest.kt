package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class ImportSaveSelectionTest {
  @Test
  fun `toggle selects and deselects id`() {
    val selected = ImportSaveSelection().toggle("q-1")
    assertTrue(selected.contains("q-1"))
    assertFalse(selected.toggle("q-1").contains("q-1"))
  }
}
