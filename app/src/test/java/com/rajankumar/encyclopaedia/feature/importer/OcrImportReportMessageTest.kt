package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class OcrImportReportMessageTest {
  @Test fun reportsDraftsAndExcludedLines() {
    val report = OcrImportReport(2, 1, OcrMetadataConfidence.PARTIAL, null)
    assertEquals("Prepared 2 review drafts. Excluded 1 suspect OCR line.", report.message())
  }
}
