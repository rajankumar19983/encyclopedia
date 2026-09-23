package com.rajankumar.encyclopaedia.feature.performance

import org.junit.Assert.assertEquals
import org.junit.Test

class PerformanceEvidenceMessageTest {
  @Test fun summaryIncludesAllEvidenceDimensions() {
    assertEquals("80% accuracy • 50% coverage • high confidence", PerformanceEvidence(AccuracySample(8,10), PerformanceCoverage(5,10), PerformanceConfidence.HIGH).message())
  }
}
