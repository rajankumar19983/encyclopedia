package com.rajankumar.encyclopaedia.feature.importer

import org.junit.Assert.assertTrue
import org.junit.Test

class OcrReviewSessionMessageTest {
  @Test fun incompleteMessageReportsPending() = assertTrue(OcrReviewSession(4, 2, 1).message().contains("1 of 4"))
  @Test fun completeMessageReportsDecisions() = assertTrue(OcrReviewSession(4, 3, 1).message().contains("3 approved"))
}
