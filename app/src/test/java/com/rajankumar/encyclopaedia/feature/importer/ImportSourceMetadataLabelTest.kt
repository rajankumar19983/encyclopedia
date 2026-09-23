package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class ImportSourceMetadataLabelTest {
  @Test fun showsDetectedExamAndYear() {
    assertEquals("DSSSB • 2024", sourceMetadataSummary(OcrSourceMetadata("DSSSB", 2024)))
  }

  @Test fun clearlyReportsMissingMetadata() {
    assertEquals("Source metadata not detected", sourceMetadataSummary(OcrSourceMetadata()))
  }
}
