package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertEquals
import org.junit.Assert.assertFalse
import org.junit.Assert.assertTrue
import org.junit.Test

class OcrTextQualityTest {
  @Test fun cleanEnglishTextDoesNotNeedReview() = assertFalse(assessOcrTextQuality("Which protocol uses port 53?").needsReview)

  @Test fun replacementCharacterNeedsReview() {
    val quality = assessOcrTextQuality("Which protoc\uFFFDl?")
    assertEquals(1, quality.suspiciousReplacementCharacters)
    assertTrue(quality.needsReview)
  }

  @Test fun devanagariNeedsReview() = assertTrue(assessOcrTextQuality("Question प्रश्न").needsReview)

  @Test fun textWithoutLatinContentNeedsReview() = assertTrue(assessOcrTextQuality("1234 ???").needsReview)
}
