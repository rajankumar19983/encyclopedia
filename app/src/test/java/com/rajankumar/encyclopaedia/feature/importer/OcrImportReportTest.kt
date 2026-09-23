package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertNull
import org.junit.Test

class OcrImportReportTest {
  @Test fun summarizesPreparedImport() {
    val preparation = prepareOcrImport("DSSSB 2024\n1. What is RAM?\nA. Memory\nB. Storage\nAnswer: A")
    val report = preparation.report()
    assertEquals(1, report.draftCount)
    assertEquals(OcrMetadataConfidence.COMPLETE, report.metadataConfidence)
    assertNull(report.structuralWarning)
  }
}
