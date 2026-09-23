package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class ImportSourceLabelTest {
  @Test fun combinesSourceExamAndYear() {
    assertEquals("PDF • DSSSB • 2025", importSourceLabel("PDF", OcrSourceMetadata("DSSSB", 2025)))
  }

  @Test fun omitsUnknownMetadata() {
    assertEquals("SCAN", importSourceLabel("SCAN", OcrSourceMetadata()))
  }
}
