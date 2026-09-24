package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class OcrMetadataMergeTest {
  @Test fun metadataCanBeRecoveredAcrossDifferentPages() {
    val metadata = extractMetadataAcrossPages(listOf(
      OcrPageText(1, "DSSSB TGT Computer Science"),
      OcrPageText(2, "Exam date 14/09/2025"),
      OcrPageText(3, "Shift II")
    ))
    assertEquals("DSSSB", metadata.examName)
    assertEquals(2025, metadata.year)
    assertEquals("2025-09-14", metadata.examDate)
    assertEquals("SHIFT 2", metadata.shift)
  }

  @Test fun failedPagesDoNotContributeMetadata() {
    val metadata = extractMetadataAcrossPages(listOf(
      OcrPageText(1, "BPSC 2026"),
      OcrPageText(2, "DSSSB 2025", "OCR failed")
    ))
    assertEquals("BPSC", metadata.examName)
    assertEquals(2026, metadata.year)
  }
}
