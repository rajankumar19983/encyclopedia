package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Test

class OcrMetadataCandidateTest {
  @Test fun previewIsBoundedAndUsesBeginningOfSource() {
    val text = (1..20).joinToString("\n") { "Header line $it" }
    val candidate = detectOcrMetadataCandidate(text)
    assertEquals("Header line 1 Header line 2 Header line 3 Header line 4 Header line 5", candidate.sourcePreview)
  }
}
